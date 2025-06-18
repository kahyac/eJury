package amu.eJury.service.Impl;

import amu.eJury.model.pedagogy.*;
import amu.eJury.model.result.*;
import amu.eJury.model.student.Student;
import amu.eJury.service.api.BonusPolicy;
import amu.eJury.service.api.StatusPropagationRule;
import amu.eJury.service.impl.DefaultSemestrialCalculatorImpl;
import amu.eJury.service.impl.DefaultStatusPropagationRule;
import amu.eJury.service.port.GradePort;
import amu.eJury.service.port.SaveSemestrialResultPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultSemestrialCalculatorImplTest {

    GradePort gradePort     = mock(GradePort.class);
    SaveSemestrialResultPort savePort = r -> r;           // pas besoin de mock
    BonusPolicy   bonus     = (s,sem) -> 0.5;             // bonus fixe 0.5
    StatusPropagationRule rule = new DefaultStatusPropagationRule();

    DefaultSemestrialCalculatorImpl calc;

    Student alice;
    SemestrialKnowledgeBlock block;
    TeachingUnit UE1, UE2;

    @BeforeEach
    void setUp() {
        calc = new DefaultSemestrialCalculatorImpl(gradePort, savePort, bonus, rule);

        alice = Student.builder().id(1L).identifiant("alice").build();

        UE1 = TeachingUnit.builder().code("UE1").build();
        UE2 = TeachingUnit.builder().code("UE2").build();

        block = SemestrialKnowledgeBlock.builder()
                .semester(1)
                .unitsCoefficientAssociation(Map.of(UE1, 2.0, UE2, 1.0))
                .build();
    }

    @Test void average_is_weighted_and_bonus_applied() {
        when(gradePort.gradeOf(alice, UE1))
                .thenReturn(Optional.of(grade(15)));
        when(gradePort.gradeOf(alice, UE2))
                .thenReturn(Optional.of(grade(9)));

        SemestrialBlockResult res = calc.compute(alice, block);

        // moyenne pondérée : (15*2 + 9*1)/3 = 13.0 + bonus 0.5 = 13.5
        assertThat(res.getAverage()).isEqualTo(13.5);
        assertThat(res.getStatus()).isEqualTo(ExceptionalStatus.NONE);
        verify(gradePort, times(2)).gradeOf(eq(alice), any());
    }

    @Test void exceptional_status_blocks_calculation() {
        when(gradePort.gradeOf(alice, UE1))
                .thenReturn(Optional.of(grade(12)));
        when(gradePort.gradeOf(alice, UE2))
                .thenReturn(Optional.of(gradeWithStatus(ExceptionalStatus.ABJ)));

        SemestrialBlockResult res = calc.compute(alice, block);

        assertThat(res.getAverage()).isNull();
        assertThat(res.getStatus()).isEqualTo(ExceptionalStatus.ABJ);
    }

    /* ------------ helpers -------------- */
    private TeachingUnitGrade grade(double g){
        return TeachingUnitGrade.builder().grade(g).status(ExceptionalStatus.NONE).build();
    }
    private TeachingUnitGrade gradeWithStatus(ExceptionalStatus st){
        return TeachingUnitGrade.builder().status(st).build();
    }
}
