package amu.eJury.service.impl;

import amu.eJury.dao.AnnualKnowledgeBlockResultRepository;
import amu.eJury.dao.AcademicYearResultRepository;
import amu.eJury.model.pedagogy.AnnualKnowledgeBlock;
import amu.eJury.model.pedagogy.CurriculumPlan;
import amu.eJury.model.result.*;
import amu.eJury.model.users.Student;
import amu.eJury.service.api.AnnualKnowledgeBlockCalculatorService;
import amu.eJury.service.api.AcademicYearDecisionCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultAcademicYearDecisionCalculatorService implements AcademicYearDecisionCalculatorService {

    private final AnnualKnowledgeBlockCalculatorService annualCalc;                 // réutilise l’algo précédent
    private final AnnualKnowledgeBlockResultRepository annualResRepo;
    private final AcademicYearResultRepository yearResRepo;

    @Transactional
    @Override
    public AcademicYearResult compute(Student s, CurriculumPlan plan) {

        boolean exceptional = false;
        int failures = 0;
        int almost   = 0;

        double totalAverage = 0.0;
        int count = 0;

        /* boucle sur les blocs annuels de la maquette */
        for (AnnualKnowledgeBlock ab : plan.getAnnualKnowledgeBlocks()) {

            AnnualKnowledgeBlockResult r = annualCalc.compute(s, ab);   // (re)calcule et sauvegarde

            if (r.getStatus() != ExceptionalStatus.NONE) {     // ABI / ABJ / AR  -> décision miroir
                exceptional = true;
                break;
            }

            double m = r.getAverage();
            totalAverage += m;
            count++;
            if (m < 9.5) failures++;
            else if (m < 10) almost++;
        }

        Mention mention = null;
        AcademicDecision decision;
        if (exceptional) {
            decision = AcademicDecision.valueOf(   // on reflète le statut bloquant
                    worstExceptionalStatus(s, plan).name());
        } else if (failures == 0 && almost <= 1) {
            decision = AcademicDecision.ADM;

            double average = totalAverage / count;

            if (average < 12) mention = Mention.PASS;
            else if (average < 14) mention = Mention.FAIRLY_GOOD;
            else if (average < 16) mention = Mention.GOOD;
            else if (average < 18) mention = Mention.VERY_GOOD;
            else mention = Mention.EXCELLENT;

        } else {
            decision = AcademicDecision.AJ;
        }

        AcademicYearResult result = yearResRepo.findByStudent(s).orElseGet(() -> new AcademicYearResult());
        result.setStudent(s);
        result.setDecision(decision);
        result.setMention(mention);

        return yearResRepo.save(result);
    }

    /* ----------- statut exceptionnel le plus sévère ----------- */
    private ExceptionalStatus worstExceptionalStatus(Student s, CurriculumPlan plan) {
        return plan.getAnnualKnowledgeBlocks().stream()
                .map(ab -> annualResRepo.findByStudentAndAnnualBlock(s, ab)
                        .map(AnnualKnowledgeBlockResult::getStatus)
                        .orElse(ExceptionalStatus.NONE))
                .reduce(ExceptionalStatus.NONE, this::propagate);
    }

    private ExceptionalStatus propagate(ExceptionalStatus a, ExceptionalStatus b) {
        if (a == ExceptionalStatus.ABI || b == ExceptionalStatus.ABI) return ExceptionalStatus.ABI;
        if (a == ExceptionalStatus.ABJ || b == ExceptionalStatus.ABJ) return ExceptionalStatus.ABJ;
        if (a == ExceptionalStatus.AR  || b == ExceptionalStatus.AR)  return ExceptionalStatus.AR;
        return ExceptionalStatus.NONE;
    }
}
