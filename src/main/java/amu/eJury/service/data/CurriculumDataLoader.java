package amu.eJury.service.data;

import amu.eJury.dao.*;
import amu.eJury.model.pedagogy.*;
import amu.eJury.model.registration.PedagogicalRegistration;
import amu.eJury.model.result.ExceptionalStatus;
import amu.eJury.model.result.TeachingUnitGrade;
import amu.eJury.model.users.Student;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@DependsOn("studentDataLoader")
@RequiredArgsConstructor
public class CurriculumDataLoader {

    private final CurriculumPlanRepository          planRepo;
    private final TeachingUnitRepository            ueRepo;
    private final StudentRepository                 studentRepo;
    private final PedagogicalRegistrationRepository regRepo;
    private final TeachingUnitGradeRepository       gradeRepo;

    @PostConstruct
    @Transactional
    public void loadCurriculumAndRegistrations() {
        if (planRepo.count() > 0) return;                   // déjà chargé

        /* 1 ────────── UE */
        /* 1 ────────── UE */
        List<TeachingUnit> ues = List.of(
                TeachingUnit.builder().code("SINA09A").label("Réseaux").semester(1).ects(7).workloadHours(70).obligation(true).build(),
                TeachingUnit.builder().code("SINA09B").label("Complexité").semester(1).ects(6).workloadHours(60).obligation(true).build(),
                TeachingUnit.builder().code("SINAX01").label("Cryptographie").semester(1).ects(2).workloadHours(20).obligation(false).build(),
                TeachingUnit.builder().code("SINA09D").label("Programmation Objet Concurrente").semester(1).ects(15).workloadHours(60).obligation(true).build(),
                TeachingUnit.builder().code("SINB34B").label("Des Conteneurs au Cloud : Principes et Administrations").semester(2).ects(15).workloadHours(40).obligation(true).build(),
                TeachingUnit.builder().code("SINB34A").label("Interface Homme-Machine").semester(2).ects(15).workloadHours(100).obligation(false).build(),
                TeachingUnit.builder().code("SINBX07").label("Informatique Quantique").semester(2).ects(15).workloadHours(25).obligation(false).build()
        );
        ueRepo.saveAll(ues);
        Map<String, TeachingUnit> ueByCode = ues.stream()
                .collect(HashMap::new, (m,u)->m.put(u.getCode(),u), HashMap::putAll);

        /* 2 ────────── BCC semestriels */
        SemestrialKnowledgeBlock bcc1S1 = SemestrialKnowledgeBlock.builder()
                .code("BCC1S1").label("BCC1 Semestre 1").semester(1).ects(26)
                .unitsCoefficientAssociation(Map.of(
                        ueByCode.get("SINA09A"), 7.0,
                        ueByCode.get("SINA09B"), 6.0,
                        ueByCode.get("SINAX01"), 2.0,
                        ueByCode.get("SINA09D"), 6.0))
                .build();

        SemestrialKnowledgeBlock bcc1S2 = SemestrialKnowledgeBlock.builder()
                .code("BCC1S2").label("BCC1 Semestre 2").semester(2).ects(9)
                .unitsCoefficientAssociation(Map.of(
                        ueByCode.get("SINB34B"), 4.0))
                .build();

        SemestrialKnowledgeBlock bcc2S1 = SemestrialKnowledgeBlock.builder()
                .code("BCC2S1").label("BCC2 Semestre 1").semester(1).ects(12)
                .unitsCoefficientAssociation(Map.of(
                        ueByCode.get("SINA09A"), 6.0,
                        ueByCode.get("SINA09B"), 7.0,
                        ueByCode.get("SINAX01"), 2.0))
                .build();

        SemestrialKnowledgeBlock bcc2S2 = SemestrialKnowledgeBlock.builder()
                .code("BCC2S2").label("BCC2 Semestre 2").semester(2).ects(13)
                .unitsCoefficientAssociation(Map.of(
                        ueByCode.get("SINB34A"), 6.0,
                        ueByCode.get("SINB34B"), 5.0,
                        ueByCode.get("SINBX07"), 2.5))
                .build();

        /* 3 ────────── BCC annuels */
        AnnualKnowledgeBlock bcc1 = AnnualKnowledgeBlock.builder()
                .code("BCC1").semesters(List.of(bcc1S1, bcc1S2)).build();
        AnnualKnowledgeBlock bcc2 = AnnualKnowledgeBlock.builder()
                .code("BCC2").semesters(List.of(bcc2S1, bcc2S2)).build();

        bcc1.getSemesters().forEach(s -> s.setAnnualKnowledgeBlock(bcc1));
        bcc2.getSemesters().forEach(s -> s.setAnnualKnowledgeBlock(bcc2));

        /* 4 ────────── Maquette */
        CurriculumPlan plan = CurriculumPlan.builder()
                .id(1L)
                .academicYear("2024/2025")
                .name("M1 Informatique")
                .annualKnowledgeBlocks(List.of(bcc1, bcc2))
                .build();
        planRepo.save(plan);

        /* 5 ────────── Inscriptions pédagogiques + notes */
        Random rng = new Random();
        List<PedagogicalRegistration> regs  = new ArrayList<>();
        List<TeachingUnitGrade>       notes = new ArrayList<>();

        List<Student> students = studentRepo.findAll();

        if (students.size() >= 3) {
            students.get(0).setLastName("KARTOUT");
            students.get(0).setFirstName("Ahmed Yacine");

            students.get(1).setLastName("BELHADJ");
            students.get(1).setFirstName("Akram Djalal");

            students.get(2).setLastName("MUFUTA MULUMBA");
            students.get(2).setFirstName("Nathan");

            studentRepo.saveAll(students.subList(0, 3));
        }
        int studentIndex = 0;

        for (Student stu : students) {
            boolean optedUE1 = false;
            boolean optedUE2 = false;

            for (TeachingUnit ue : ues) {
                int semester = (ue.getCode().startsWith("SINB34B") || ue.getCode().startsWith("SINB34A") || ue.getCode().equals("SINBX07")) ? 2 : 1;

                // Assigner exactement une UE optionnelle par semestre
                if (!ue.isObligation()) {
                    if ((semester == 1 && optedUE1) || (semester == 2 && optedUE2)) continue;
                    if (semester == 1) optedUE1 = true;
                    if (semester == 2) optedUE2 = true;
                }

                PedagogicalRegistration reg = PedagogicalRegistration.builder()
                        .student(stu)
                        .teachingUnit(ue)
                        .semester(semester)
                        .build();
                regs.add(reg);

                // Générer une note ou un statut exceptionnel selon l'étudiant
                ExceptionalStatus status = ExceptionalStatus.NONE;
                Double grade = null;

                switch (studentIndex) {
                    case 0 -> {
                        // Étudiant 0 : ABI sur UE1
                        if (ue.getCode().equals("SINA09A")) {
                            grade = null;
                            status = ExceptionalStatus.ABI;
                        } else {
                            grade = Math.round((8 + rng.nextDouble() * 10) * 10) / 10.0;
                            status = ExceptionalStatus.NONE;
                        }
                    }
                    case 1 -> {
                        // Étudiant 1 : ABJ sur UE2
                        if (ue.getCode().equals("SINA09B")) {
                            grade = null;
                            status = ExceptionalStatus.ABJ;
                        } else {
                            grade = Math.round((8 + rng.nextDouble() * 10) * 10) / 10.0;
                            status = ExceptionalStatus.NONE;
                        }
                    }
                    case 2 -> {
                        // Étudiant 2 : AR sur UE3
                        if (ue.getCode().equals("SINAX01")) {
                            grade = null;
                            status = ExceptionalStatus.AR;
                        } else {
                            grade = Math.round((8 + rng.nextDouble() * 10) * 10) / 10.0;
                            status = ExceptionalStatus.NONE;
                        }
                    }
                    case 3 -> {
                        // Étudiant 3 : ABI sur UE1, ABJ sur UE4
                        if (ue.getCode().equals("SINA09A")) {
                            grade = null;
                            status = ExceptionalStatus.ABI;
                        } else if (ue.getCode().equals("SINA09D")) {
                            grade = null;
                            status = ExceptionalStatus.ABJ;
                        } else {
                            grade = Math.round((8 + rng.nextDouble() * 10) * 10) / 10.0;
                            status = ExceptionalStatus.NONE;
                        }
                    }
                    case 4 -> {
                        // Étudiant 4 : ABJ sur UE5, AR sur UE6
                        if (ue.getCode().equals("SINB34B")) {
                            grade = null;
                            status = ExceptionalStatus.ABJ;
                        } else if (ue.getCode().equals("SINB34A")) {
                            grade = null;
                            status = ExceptionalStatus.AR;
                        } else {
                            grade = Math.round((8 + rng.nextDouble() * 10) * 10) / 10.0;
                            status = ExceptionalStatus.NONE;
                        }
                    }
                    default -> {
                        grade = Math.round((8 + rng.nextDouble() * 10) * 10) / 10.0;
                        status = ExceptionalStatus.NONE;
                    }
                }


                notes.add(TeachingUnitGrade.builder()
                        .registration(reg)
                        .grade(grade)
                        .status(status)
                        .build());
            }

            studentIndex++;
        }

        regRepo.saveAll(regs);
        gradeRepo.saveAll(notes);

        System.out.printf("⚙️  Chargé : %d UE, %d étudiants, %d IP, %d notes%n",
                ues.size(), studentRepo.count(), regs.size(), notes.size());
    }
}
