package amu.eJury.service.Impl;

import amu.eJury.dao.AnnualKnowledgeBlockResultRepository;
import amu.eJury.dao.SemestrialKnowledgeBlockResultRepository;
import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.result.*;
import amu.eJury.model.users.Student;
import amu.eJury.service.impl.DefaultAnnualKnowledgeBlockCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultAnnualCalculatorTest {

    SemestrialKnowledgeBlockResultRepository semRepo = mock(SemestrialKnowledgeBlockResultRepository.class);
    AnnualKnowledgeBlockResultRepository annRepo = mock(AnnualKnowledgeBlockResultRepository.class);

    DefaultAnnualKnowledgeBlockCalculatorService calc;
    Student alice;
    AnnualKnowledgeBlock annual;
    SemestrialKnowledgeBlockResult s1, s2;

    @BeforeEach
    void init() {
        semRepo = mock(SemestrialKnowledgeBlockResultRepository.class);
        annRepo = mock(AnnualKnowledgeBlockResultRepository.class);
        when(annRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        calc = new DefaultAnnualKnowledgeBlockCalculatorService(semRepo, annRepo);
        alice = Student.builder().id(1L).identifiant("alice").build();

        SemestrialKnowledgeBlock sem1 = SemestrialKnowledgeBlock.builder().semester(1).ects(20).build();
        SemestrialKnowledgeBlock sem2 = SemestrialKnowledgeBlock.builder().semester(2).ects(40).build();
        annual = AnnualKnowledgeBlock.builder().semesters(List.of(sem1, sem2)).build();

        s1 = SemestrialKnowledgeBlockResult.builder().average(12.0).status(ExceptionalStatus.NONE).build();
        s2 = SemestrialKnowledgeBlockResult.builder().average(9.0).status(ExceptionalStatus.NONE).build();
    }

    @Test
    void weighted_average_is_correct() {
        when(semRepo.findByStudentAndSemBlock_SemesterAndSemBlock_AnnualKnowledgeBlock(alice,1,annual))
                .thenReturn(Optional.of(s1));
        when(semRepo.findByStudentAndSemBlock_SemesterAndSemBlock_AnnualKnowledgeBlock(alice,2,annual))
                .thenReturn(Optional.of(s2));

        // annRepo.save(...) déjà stubé dans @BeforeEach
        AnnualKnowledgeBlockResult res = calc.compute(alice, annual);

        assertThat(res).isNotNull();
        assertThat(res.getAverage()).isEqualTo(10.0);
    }


    @Test void exceptional_status_propagates() {
        s2.setStatus(ExceptionalStatus.ABI);
        when(semRepo.findByStudentAndSemBlock_SemesterAndSemBlock_AnnualKnowledgeBlock(alice,1,annual))
                .thenReturn(Optional.of(s1));
        when(semRepo.findByStudentAndSemBlock_SemesterAndSemBlock_AnnualKnowledgeBlock(alice,2,annual))
                .thenReturn(Optional.of(s2));

        AnnualKnowledgeBlockResult res = calc.compute(alice, annual);

        assertThat(res.getAverage()).isNull();
        assertThat(res.getStatus()).isEqualTo(ExceptionalStatus.ABI);
    }
}
