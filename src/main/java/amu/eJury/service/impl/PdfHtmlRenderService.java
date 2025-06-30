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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Date;
import java.time.ZoneId;

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
        context.setVariable("now", Date.from(LocalDateTime.now()
                .atZone(ZoneId.systemDefault()).toInstant()));
        String signatureBase64 = "";
        try {
            Path path = Path.of("src/main/resources/static/images/logo-signature.png");
            byte[] imageBytes = Files.readAllBytes(path);
            signatureBase64 = Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture de l'image de signature : " + e.getMessage());
        }
        System.out.println("Signature Base64 length: " + signatureBase64.length());
        System.out.println("Signature preview: " + signatureBase64.substring(0, 30) + "...");

        context.setVariable("signatureBase64", signatureBase64);

        return templateEngine.process("pv-template", context);
    }
}
