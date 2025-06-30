package amu.eJury.service.Impl;

import amu.eJury.dao.AnnualKnowledgeBlockResultRepository;
import amu.eJury.dao.AcademicYearResultRepository;
import amu.eJury.model.pedagogy.*;
import amu.eJury.model.result.*;
import amu.eJury.model.users.Student;
import amu.eJury.service.api.AnnualKnowledgeBlockCalculatorService;
import amu.eJury.service.impl.DefaultAcademicYearDecisionCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultAcademicYearDecisionCalculatorServiceTest {

    AnnualKnowledgeBlockCalculatorService annCalc = mock(AnnualKnowledgeBlockCalculatorService.class);
    AnnualKnowledgeBlockResultRepository annRepo = mock(AnnualKnowledgeBlockResultRepository.class);
    AcademicYearResultRepository yearRepo =mock(AcademicYearResultRepository.class);

    DefaultAcademicYearDecisionCalculatorService calc;

    Student alice;
    CurriculumPlan plan;
    AnnualKnowledgeBlock b1, b2;

    @BeforeEach
    void setUp() {
        annCalc  = mock(AnnualKnowledgeBlockCalculatorService.class);

        annRepo  = mock(AnnualKnowledgeBlockResultRepository.class);
        when(annRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        yearRepo = mock(AcademicYearResultRepository.class);
        when(yearRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        calc = new DefaultAcademicYearDecisionCalculatorService(annCalc, annRepo, yearRepo);

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
        AnnualKnowledgeBlockResult abjRes = AnnualKnowledgeBlockResult.builder()
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
    private AnnualKnowledgeBlockResult blockRes(double avg){
        return AnnualKnowledgeBlockResult.builder()
                .average(avg).status(ExceptionalStatus.NONE).build();
    }

    @Test void mention_fairly_good_if_average_between_12_and_14() {
        when(annCalc.compute(alice, b1)).thenReturn(blockRes(12.5));
        when(annCalc.compute(alice, b2)).thenReturn(blockRes(13.5));

        AcademicYearResult res = calc.compute(alice, plan);
        assertThat(res.getMention()).isEqualTo(Mention.FAIRLY_GOOD);
    }

    @Test void mention_good_if_average_between_14_and_16() {
        when(annCalc.compute(alice, b1)).thenReturn(blockRes(14.1));
        when(annCalc.compute(alice, b2)).thenReturn(blockRes(15.3));

        AcademicYearResult res = calc.compute(alice, plan);
        assertThat(res.getMention()).isEqualTo(Mention.GOOD);
    }

    @Test void mention_very_good_if_average_between_16_and_18() {
        when(annCalc.compute(alice, b1)).thenReturn(blockRes(16.0));
        when(annCalc.compute(alice, b2)).thenReturn(blockRes(17.8));

        AcademicYearResult res = calc.compute(alice, plan);
        assertThat(res.getMention()).isEqualTo(Mention.VERY_GOOD);
    }

    @Test void mention_excellent_if_average_above_18() {
        when(annCalc.compute(alice, b1)).thenReturn(blockRes(18.2));
        when(annCalc.compute(alice, b2)).thenReturn(blockRes(19.4));

        AcademicYearResult res = calc.compute(alice, plan);
        assertThat(res.getMention()).isEqualTo(Mention.EXCELLENT);
    }
}