package amu.eJury.service.Impl;

import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.result.*;
import amu.eJury.model.users.Student;
import amu.eJury.service.api.BonusPolicy;
import amu.eJury.service.api.StatusPropagationRuleService;
import amu.eJury.service.impl.DefaultSemestrialKnowledgeBlockCalculatorServiceImpl;
import amu.eJury.service.impl.DefaultStatusPropagationRuleService;
import amu.eJury.service.port.GradePort;
import amu.eJury.service.port.SaveSemestrialResultPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultSemestrialKnowledgeBlockCalculatorServiceImplTest {

    GradePort gradePort = mock(GradePort.class);
    SaveSemestrialResultPort savePort = mock(SaveSemestrialResultPort.class);
    BonusPolicy bonus = (s, sem) -> 0.5; // bonus fixe
    StatusPropagationRuleService rule = new DefaultStatusPropagationRuleService();

    DefaultSemestrialKnowledgeBlockCalculatorServiceImpl calc;

    Student alice;
    SemestrialKnowledgeBlock block;
    TeachingUnit UE1, UE2;

    @BeforeEach
    void setUp() {
        when(savePort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(savePort.findByStudentAndBlock(any(), any())).thenReturn(Optional.empty());

        calc = new DefaultSemestrialKnowledgeBlockCalculatorServiceImpl(gradePort, savePort, bonus, rule);

        alice = Student.builder().id(1L).identifiant("alice").build();

        UE1 = TeachingUnit.builder().code("UE1").build();
        UE2 = TeachingUnit.builder().code("UE2").build();

        block = SemestrialKnowledgeBlock.builder()
                .semester(1)
                .unitsCoefficientAssociation(Map.of(UE1, 2.0, UE2, 1.0))
                .build();
    }

    @Test void average_is_weighted_and_bonus_applied() {
        // Mocks pour UE1 et UE2
        PedagogicalRegistration reg1 = mock(PedagogicalRegistration.class);
        when(reg1.getTeachingUnit()).thenReturn(UE1);
        when(reg1.getSemester()).thenReturn(1);
        when(reg1.getGradeInfo()).thenReturn(grade(15));

        PedagogicalRegistration reg2 = mock(PedagogicalRegistration.class);
        when(reg2.getTeachingUnit()).thenReturn(UE2);
        when(reg2.getSemester()).thenReturn(1);
        when(reg2.getGradeInfo()).thenReturn(grade(9));

        when(gradePort.registrationsOf(alice)).thenReturn(List.of(reg1, reg2));

        SemestrialKnowledgeBlockResult res = calc.compute(alice, block);

        // moyenne pondérée : (15*2 + 9*1)/3 = 13.0 + bonus 0.5 = 13.5
        assertThat(res.getAverage()).isEqualTo(13.5);
        assertThat(res.getStatus()).isEqualTo(ExceptionalStatus.NONE);
    }

    @Test void exceptional_status_blocks_calculation() {
        PedagogicalRegistration reg1 = mock(PedagogicalRegistration.class);
        when(reg1.getTeachingUnit()).thenReturn(UE1);
        when(reg1.getSemester()).thenReturn(1);
        when(reg1.getGradeInfo()).thenReturn(grade(12));

        PedagogicalRegistration reg2 = mock(PedagogicalRegistration.class);
        when(reg2.getTeachingUnit()).thenReturn(UE2);
        when(reg2.getSemester()).thenReturn(1);
        when(reg2.getGradeInfo()).thenReturn(gradeWithStatus(ExceptionalStatus.ABJ));

        when(gradePort.registrationsOf(alice)).thenReturn(List.of(reg1, reg2));

        SemestrialKnowledgeBlockResult res = calc.compute(alice, block);

        assertThat(res.getAverage()).isNull(); // bloqué par ABJ
        assertThat(res.getStatus()).isEqualTo(ExceptionalStatus.ABJ);
    }

    // Helpers
    private TeachingUnitGrade grade(double g) {
        return TeachingUnitGrade.builder().grade(g).status(ExceptionalStatus.NONE).build();
    }

    private TeachingUnitGrade gradeWithStatus(ExceptionalStatus st) {
        return TeachingUnitGrade.builder().status(st).build();
    }
}
