package amu.eJury.controller;

import amu.eJury.dao.*;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.result.*;
import amu.eJury.model.users.Student;
import amu.eJury.model.users.AppUser;
import amu.eJury.model.users.Teacher;
import amu.eJury.service.dtos.TeachingUnitGradeFormDTO;
import amu.eJury.service.impl.PdfHtmlRenderService;
import amu.eJury.service.utils.PDFUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
@RequestMapping("/grades")
@RequiredArgsConstructor
public class TeachingUnitGradeController {

    private final StudentRepository studentRepository;
    private final TeachingUnitRepository teachingUnitRepository;
    private final TeachingUnitGradeRepository teachingUnitGradeRepository;
    private final PedagogicalRegistrationRepository pedagogicalRegistrationRepository;
    private final AppUserRepository appUserRepository;
    private final TeachingUnitGradeRepository gradeRepo;
    private final SemestrialKnowledgeBlockResultRepository semResultRepo;
    private final AnnualKnowledgeBlockResultRepository annualResultRepo;
    private final AcademicYearResultRepository yearResultRepo;
    private final PdfHtmlRenderService htmlRenderService;


    @GetMapping("/new")
    public String showForm(Model model,
                           @ModelAttribute("currentUser") AppUser currentUser) {

        model.addAttribute("gradeForm",
                new TeachingUnitGradeFormDTO(null, null, "NONE", null));
        model.addAttribute("students", studentRepository.findAll());

        if (currentUser.getTeacher() != null) {
            model.addAttribute("units", currentUser.getTeacher().getTeachingUnits());
        } else {
            model.addAttribute("units", teachingUnitRepository.findAll());
        }

        return "grade/saisie-note";
    }


    @PostMapping("/save")
    public String saveGrade(@ModelAttribute("gradeForm") TeachingUnitGradeFormDTO form,
                            RedirectAttributes redirectAttributes) {

        var registrationOpt = pedagogicalRegistrationRepository
                .findByStudentIdAndTeachingUnitId(form.studentId(), form.unitId());

        if (registrationOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Impossible d’enregistrer : l’étudiant sélectionné n’est pas inscrit à cette unité d’enseignement.");
            return "redirect:/grades/new";
        }

        PedagogicalRegistration registration = registrationOpt.get();

        TeachingUnitGrade grade = registration.getGradeInfo();

        if (grade == null) {
            grade = TeachingUnitGrade.builder()
                    .registration(registration)
                    .build();
        }

        grade.setStatus(ExceptionalStatus.valueOf(form.status()));
        grade.setGrade(form.status().equals("NONE") ? form.numeric() : null);

        teachingUnitGradeRepository.save(grade);

        redirectAttributes.addFlashAttribute("successMessage", "Note ou statut enregistré(e) avec succès.");
        return "redirect:/grades/new";
    }

    @GetMapping("/view")
    public String showGradeLookupForm(Model model,
                                      @ModelAttribute("currentStudent") Student currentStudent,
                                      @ModelAttribute("currentUser") AppUser currentUser) {

        if (currentStudent != null) {
            // Cas étudiant
            model.addAttribute("students", List.of(currentStudent));
            model.addAttribute("selectedStudentId", currentStudent.getId());
            model.addAttribute("units", teachingUnitRepository.findAll());
        } else if (currentUser != null && currentUser.getTeacher() != null) {
            // Cas enseignant ou admin responsable d’UE
            Teacher teacher = currentUser.getTeacher();
            model.addAttribute("students", studentRepository.findAll());
            model.addAttribute("units", teacher.getTeachingUnits()); // juste ses UEs
        } else {
            // Cas admin sans rôle enseignant
            model.addAttribute("students", studentRepository.findAll());
            model.addAttribute("units", teachingUnitRepository.findAll());
        }

        return "grade/view-grades";
    }



    @PostMapping("/view")
    public String displayGrade(@RequestParam Long studentId,
                               @RequestParam Long unitId,
                               @ModelAttribute("currentStudent") Student currentStudent,
                               @ModelAttribute("currentUser") AppUser currentUser,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        boolean isStudent = (currentStudent != null);
        boolean isAdmin = (currentUser != null && currentUser.hasRole("ADMIN"));
        boolean isTeacher = (currentUser != null && currentUser.hasRole("TEACHER"));

        if (isStudent && !studentId.equals(currentStudent.getId())) {
            redirectAttributes.addFlashAttribute("message", "Accès non autorisé.");
            return "redirect:/grades/view";
        }

        var registrationOpt = pedagogicalRegistrationRepository
                .findByStudentIdAndTeachingUnitId(studentId, unitId);

        if (registrationOpt.isEmpty() || registrationOpt.get().getGradeInfo() == null) {
            model.addAttribute("message", "Aucune note disponible pour cette UE.");
            model.addAttribute("grade", null);
        } else {
            var grade = registrationOpt.get().getGradeInfo();
            model.addAttribute("grade", grade);
        }

        model.addAttribute("selectedStudentId", studentId);
        model.addAttribute("studentId", studentId);
        model.addAttribute("selectedUnitId", unitId);

        if (isStudent) {
            model.addAttribute("students", List.of(currentStudent));
            model.addAttribute("units", teachingUnitRepository.findAll());
        } else if (isAdmin) {
            model.addAttribute("students", studentRepository.findAll());
            model.addAttribute("units", teachingUnitRepository.findAll());
        } else if (isTeacher) {
            model.addAttribute("students", studentRepository.findAll());
            model.addAttribute("units", currentUser.getTeacher().getTeachingUnits());
        } else {
            redirectAttributes.addFlashAttribute("message", "Accès refusé.");
            return "redirect:/grades/view";
        }

        return "grade/view-grades";
    }




    @ModelAttribute("currentStudent")
    public Student getCurrentStudent(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return null;

        return appUserRepository.findByEmail(userDetails.getUsername())
                .map(AppUser::getStudent)
                .orElse(null);
    }

    @ModelAttribute("currentUser")
    public AppUser getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return null;
        return appUserRepository.findByEmail(userDetails.getUsername()).orElse(null);
    }

    @GetMapping("/pv/download")
    public ResponseEntity<byte[]> downloadPv(@RequestParam(required = false) Long studentId,
                                             @ModelAttribute("currentStudent") Student currentStudent,
                                             @ModelAttribute("currentUser") AppUser currentUser) {
        Student target;

        if (currentUser != null && currentUser.hasRole("ADMIN") && studentId != null) {
            target = studentRepository.findById(studentId).orElse(null);
            if (target == null) return ResponseEntity.notFound().build();
        } else if (currentStudent != null) {
            target = currentStudent;
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return generatePdfFor(target);
    }



    private ResponseEntity<byte[]> generatePdfFor(Student student) {
        List<TeachingUnitGrade> grades = gradeRepo.findByRegistration_Student_Id(student.getId());
        List<SemestrialKnowledgeBlockResult> semResults = semResultRepo.findByStudent(student);
        List<AnnualKnowledgeBlockResult> annualResults = annualResultRepo.findByStudent(student);
        AcademicYearResult yearResult = yearResultRepo.findByStudent(student).orElse(null);

        String html = htmlRenderService.renderHtml(student, grades, semResults, annualResults, yearResult);
        byte[] pdf = PDFUtil.fromHtml(html);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "PV_" + student.getLastName() + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

}
