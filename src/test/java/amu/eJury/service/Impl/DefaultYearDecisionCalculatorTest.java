package amu.eJury.service.Impl;

import amu.eJury.dao.AnnualBlockResultRepository;
import amu.eJury.dao.AcademicYearResultRepository;
import amu.eJury.model.pedagogy.*;
import amu.eJury.model.result.*;
import amu.eJury.model.student.Student;
import amu.eJury.service.api.AnnualCalculator;
import amu.eJury.service.impl.DefaultYearDecisionCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultYearDecisionCalculatorTest {

    AnnualCalculator annCalc = mock(AnnualCalculator.class);
    AnnualBlockResultRepository annRepo = mock(AnnualBlockResultRepository.class);
    AcademicYearResultRepository yearRepo =mock(AcademicYearResultRepository.class);

    DefaultYearDecisionCalculator calc;

    Student alice;
    CurriculumPlan plan;
    AnnualKnowledgeBlock b1, b2;

    @BeforeEach
    void setUp() {
        annCalc  = mock(AnnualCalculator.class);

        annRepo  = mock(AnnualBlockResultRepository.class);
        when(annRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        yearRepo = mock(AcademicYearResultRepository.class);
        when(yearRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        calc = new DefaultYearDecisionCalculator(annCalc, annRepo, yearRepo);

        alice = Student.builder().id(1L).identifiant("alice").build();
        b1 = AnnualKnowledgeBlock.builder().code("B1").build();
        b2 = AnnualKnowledgeBlock.builder().code("B2").build();

        plan = CurriculumPlan.builder()
                .academicYear("2024/2025")
                .annualKnowledgeBlocks(List.of(b1, b2))
                .build();
    }

    @Test void decision_is_ADM_when_all_blocks_valid() {
        when(annCalc.compute(alice, b1))
                .thenReturn(blockRes(12));
        when(annCalc.compute(alice, b2))
                .thenReturn(blockRes(10.2));

        AcademicYearResult res = calc.compute(alice, plan);

        assertThat(res.getDecision()).isEqualTo(AcademicDecision.ADM);
    }

    @Test void decision_is_AJ_when_two_failures() {
        when(annCalc.compute(alice, b1))
                .thenReturn(blockRes(8.5));
        when(annCalc.compute(alice, b2))
                .thenReturn(blockRes(9.2));

        AcademicYearResult res = calc.compute(alice, plan);

        assertThat(res.getDecision()).isEqualTo(AcademicDecision.AJ);
    }

    @Test void single_block_between_9_5_and_10_is_compensated() {
        when(annCalc.compute(alice, b1))
                .thenReturn(blockRes(9.7));
        when(annCalc.compute(alice, b2))
                .thenReturn(blockRes(11));

        AcademicYearResult res = calc.compute(alice, plan);

        assertThat(res.getDecision()).isEqualTo(AcademicDecision.ADM);
    }

    @Test
    void exceptional_status_yields_same_decision() {
        AnnualBlockResult abjRes = AnnualBlockResult.builder()
                .average(null)
                .status(ExceptionalStatus.ABJ)
                .build();

        when(annCalc.compute(alice, b1)).thenReturn(abjRes);

        // informer worstExceptionalStatus :
        when(annRepo.findByStudentAndAnnualBlock(alice, b1))
                .thenReturn(Optional.of(abjRes));

        AcademicYearResult res = calc.compute(alice, plan);

        assertThat(res.getDecision()).isEqualTo(AcademicDecision.ABJ);
    }

    /* helper */
    private AnnualBlockResult blockRes(double avg){
        return AnnualBlockResult.builder()
                .average(avg).status(ExceptionalStatus.NONE).build();
    }
}