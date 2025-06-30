package amu.eJury.service.impl;

import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.result.ExceptionalStatus;
import amu.eJury.model.result.SemestrialKnowledgeBlockResult;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.users.Student;
import amu.eJury.service.api.BonusPolicy;
import amu.eJury.service.api.SemestrialKnowledgeBlockCalculatorService;
import amu.eJury.service.api.StatusPropagationRuleService;
import amu.eJury.service.port.GradePort;
import amu.eJury.service.port.SaveSemestrialResultPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultSemestrialKnowledgeBlockCalculatorServiceImpl implements SemestrialKnowledgeBlockCalculatorService {

    private final GradePort gradePort;                       // abstraction !
    private final SaveSemestrialResultPort savePort;
    private final BonusPolicy bonusPolicy;
    private final StatusPropagationRuleService statusRule;

    @Override
    public SemestrialKnowledgeBlockResult compute(Student s, SemestrialKnowledgeBlock block) {

        double sum = 0, sumCoef = 0;
        ExceptionalStatus worst = ExceptionalStatus.NONE;

        for (PedagogicalRegistration ip : gradePort.registrationsOf(s)) {
            TeachingUnit ue = ip.getTeachingUnit();
            if (!block.getUnitsCoefficientAssociation().containsKey(ue)) continue;
            if (ip.getSemester() != block.getSemester()) continue;

            TeachingUnitGrade g = ip.getGradeInfo();
            if (g == null) {
                throw new IllegalStateException("Grade missing for UE: " + ue.getLabel());
            }

            double coef = block.getUnitsCoefficientAssociation().get(ue);
            worst = statusRule.propagate(worst, g.getStatus());

            if (g.getStatus() == ExceptionalStatus.NONE) {
                sum += g.getGrade() * coef;
                sumCoef += coef;
            }
        }


        SemestrialKnowledgeBlockResult r = savePort.findByStudentAndBlock(s, block)
                .orElseGet(() -> SemestrialKnowledgeBlockResult.builder()
                        .student(s).semBlock(block)
                        .build());

        r.setStatus(worst);
        r.setBonusApplied(0);
        r.setAverage(null);

        if (worst == ExceptionalStatus.NONE & sumCoef > 0) {
            double avg = round(sum / sumCoef, 2);
            double bonus = bonusPolicy.bonusFor(s, block.getSemester());
            r.setBonusApplied(bonus);
            r.setAverage(Math.min(20, avg + bonus));
        }
        else {
            r.setBonusApplied(0);
            r.setAverage(null);
        }

        return savePort.save(r);

    }

    private static double round(double value, int digits) {
        double factor = Math.pow(10, digits);
        return Math.round(value * factor) / factor;
    }
}