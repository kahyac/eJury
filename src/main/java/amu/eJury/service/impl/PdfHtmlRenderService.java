package amu.eJury.service.impl;

import amu.eJury.model.result.AcademicYearResult;
import amu.eJury.model.result.AnnualKnowledgeBlockResult;
import amu.eJury.model.result.SemestrialKnowledgeBlockResult;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.users.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfHtmlRenderService {

    private final SpringTemplateEngine templateEngine;

    public String renderHtml(Student student, List<TeachingUnitGrade> unitGrades,
                             List<SemestrialKnowledgeBlockResult> semestrialResults,
                             List<AnnualKnowledgeBlockResult> annualResults,
                             AcademicYearResult yearResult) {

        Context context = new Context();
        context.setVariable("student", student);
        context.setVariable("unitGrades", unitGrades);
        context.setVariable("semestrialResults", semestrialResults);
        context.setVariable("annualResults", annualResults);
        context.setVariable("yearResult", yearResult);

        return templateEngine.process("pv-template", context);
    }
}
