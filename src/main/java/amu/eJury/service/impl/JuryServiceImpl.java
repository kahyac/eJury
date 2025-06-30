package amu.eJury.service.impl;

import amu.eJury.dao.*;
import amu.eJury.model.pedagogy.CurriculumPlan;
import amu.eJury.model.result.AcademicYearResult;
import amu.eJury.model.result.AnnualKnowledgeBlockResult;
import amu.eJury.model.result.SemestrialKnowledgeBlockResult;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.users.Student;
import amu.eJury.service.ResultEngine;
import amu.eJury.service.utils.PDFUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;


import java.util.List;

@Service
@RequiredArgsConstructor
public class JuryServiceImpl {
    private final ResultEngine engine;
    private final CurriculumPlanRepository planRepo;
    private final StudentRepository studentRepo;
    private final TeachingUnitGradeRepository gradeRepo;
    private final SemestrialKnowledgeBlockResultRepository semResultRepo;
    private final AnnualKnowledgeBlockResultRepository annualResultRepo;
    private final AcademicYearResultRepository yearResultRepo;
    private final PdfHtmlRenderService htmlRenderService;


    @Transactional
    public int runJury(Long planId) {
        CurriculumPlan plan = planRepo.findById(planId)
                .orElseThrow();
        int counter = 0;

        for (Student student : studentRepo.findAll()) {
            //1. Recalcul
            plan.getAnnualKnowledgeBlocks().stream()
                    .flatMap(ab -> ab.getSemesters().stream())
                    .forEach(sem -> engine.recompute(student, plan, sem));

            //2. Récupération des résultats après calcul
            List<TeachingUnitGrade> grades = gradeRepo.findByRegistration_Student_Id(student.getId());
            List<SemestrialKnowledgeBlockResult> semResults = semResultRepo.findByStudent(student);
            List<AnnualKnowledgeBlockResult> annualResults = annualResultRepo.findByStudent(student);
            AcademicYearResult yearResult = yearResultRepo.findByStudent(student).orElse(null);

            //3. Génération HTML
            String html = htmlRenderService.renderHtml(student, grades, semResults, annualResults, yearResult);

            //4. Conversion PDF
            try {
                byte[] pdf = PDFUtil.fromHtml(html);
                Path path = Paths.get("pv_storage/PV_" + student.getId() + ".pdf");
                Files.createDirectories(path.getParent());
                Files.write(path, pdf);
            } catch (IOException e) {
                // log propre
                System.err.println("Erreur génération PV pour " + student.getId());
                e.printStackTrace();
            }

            counter++;
        }

        return counter;
    }
}
