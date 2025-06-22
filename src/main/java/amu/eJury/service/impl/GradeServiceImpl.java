package amu.eJury.service.impl;

import amu.eJury.dao.*;
import amu.eJury.model.pedagogy.TeachingUnit;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.result.ExceptionalStatus;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.users.Student;
import amu.eJury.service.api.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final TeachingUnitRepository       ueRepo;
    private final StudentRepository            studentRepo;
    private final PedagogicalRegistrationRepository regRepo;
    private final TeachingUnitGradeRepository  gradeRepo;

    @Transactional
    @Override
    public void saveOrUpdate(long ueId, long studentId,
                             Double numeric, ExceptionalStatus status) {

        if (status == ExceptionalStatus.NONE && (numeric == null || numeric < 0 || numeric > 20))
            throw new IllegalArgumentException("Note numérique entre 0 et 20 requise.");
        if (status != ExceptionalStatus.NONE) numeric = null;      // statut => pas de note

        TeachingUnit ue = ueRepo.findById(ueId)
                .orElseThrow(() -> new IllegalArgumentException("UE inconnue"));
        Student      st = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant inconnu"));

        /* inscription pédagogique (IP) — on la crée au besoin */
        PedagogicalRegistration reg = regRepo
                .findByStudentAndTeachingUnit(st, ue)
                .orElseThrow(() -> new IllegalStateException(
                        "Aucune inscription pédagogique (IP) trouvée ; "
                                + "le responsable du parcours doit la créer avant la saisie des notes."));

        /* note/statut */
        TeachingUnitGrade grade = gradeRepo
                .findByRegistration(reg)
                .orElse(TeachingUnitGrade.builder().registration(reg).build());

        grade.setGrade(numeric);
        grade.setStatus(status);

        gradeRepo.save(grade);
    }
}
