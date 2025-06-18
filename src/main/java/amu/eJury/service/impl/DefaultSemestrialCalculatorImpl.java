package amu.eJury.service.impl;

import amu.eJury.model.pedagogy.SemestrialKnowledgeBlock;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.result.ExceptionalStatus;
import amu.eJury.model.result.SemestrialBlockResult;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.student.Student;
import amu.eJury.service.api.BonusPolicy;
import amu.eJury.service.api.SemestrialCalculator;
import amu.eJury.service.api.StatusPropagationRule;
import amu.eJury.service.port.GradePort;
import amu.eJury.service.port.SaveSemestrialResultPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultSemestrialCalculatorImpl implements SemestrialCalculator {

    private final GradePort gradePort;                       // abstraction !
    private final SaveSemestrialResultPort savePort;
    private final BonusPolicy bonusPolicy;
    private final StatusPropagationRule statusRule;

    @Override
    public SemestrialBlockResult compute(Student s, SemestrialKnowledgeBlock block) {

        double sum = 0, sumCoef = 0;
        ExceptionalStatus worst = ExceptionalStatus.NONE;

        for (var e : block.getUnitsCoefficientAssociation().entrySet()) {
            TeachingUnit ue = e.getKey();
            double coef = e.getValue();
            TeachingUnitGrade g = gradePort.gradeOf(s, ue)
                    .orElseThrow(() -> new IllegalStateException("Grade missing"));

            worst = statusRule.propagate(worst, g.getStatus());

            if (g.getStatus() == ExceptionalStatus.NONE) {
                sum += g.getGrade() * coef;
                sumCoef += coef;
            }
        }

        SemestrialBlockResult r = SemestrialBlockResult.builder()
                .student(s).semBlock(block)
                .status(worst).bonusApplied(0).average(null).build();

        if (worst == ExceptionalStatus.NONE) {
            double avg = round(sum / sumCoef, 2);
            double bonus = bonusPolicy.bonusFor(s, block.getSemester());
            r.setBonusApplied(bonus);
            r.setAverage(Math.min(20, avg + bonus));
        }
        return savePort.save(r);
    }

    private static double round(double value, int digits) {
        double factor = Math.pow(10, digits);
        return Math.round(value * factor) / factor;
    }
}