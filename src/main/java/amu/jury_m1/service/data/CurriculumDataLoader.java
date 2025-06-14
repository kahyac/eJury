package amu.jury_m1.service.data;

import amu.jury_m1.dao.*;
import amu.jury_m1.model.student.Student;
import amu.jury_m1.model.pedagogy.*;
import amu.jury_m1.model.registration.PedagogicalRegistration;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@DependsOn("studentDataLoader")
public class CurriculumDataLoader {

    private final CurriculumPlanRepository curriculumPlanRepository;
    private final TeachingUnitRepository teachingUnitRepository;
    private final StudentRepository studentRepository;
    private final PedagogicalRegistrationRepository registrationRepository;

    public CurriculumDataLoader(CurriculumPlanRepository curriculumPlanRepository,
                                TeachingUnitRepository teachingUnitRepository,
                                StudentRepository studentRepository,
                                PedagogicalRegistrationRepository registrationRepository) {
        this.curriculumPlanRepository = curriculumPlanRepository;
        this.teachingUnitRepository = teachingUnitRepository;
        this.studentRepository = studentRepository;
        this.registrationRepository = registrationRepository;
    }

    @PostConstruct
    public void loadCurriculumAndRegistrations() {
        if (curriculumPlanRepository.count() > 0) return;

        // 1. Créer les UE
        List<TeachingUnit> ues = List.of(
                TeachingUnit.builder().code("UE1").label("UE1 Automne").ects(7).workloadHours(70).obligation(true).build(),
                TeachingUnit.builder().code("UE2").label("UE2 Automne").ects(6).workloadHours(60).obligation(true).build(),
                TeachingUnit.builder().code("UE3").label("UE3 Automne").ects(2).workloadHours(20).obligation(false).build(),
                TeachingUnit.builder().code("UE4").label("UE4 Automne").ects(6).workloadHours(60).obligation(true).build(),
                TeachingUnit.builder().code("UE5").label("UE5 Printemps").ects(4).workloadHours(40).obligation(true).build(),
                TeachingUnit.builder().code("UE6").label("UE6 Printemps").ects(10).workloadHours(100).obligation(false).build(),
                TeachingUnit.builder().code("UE7").label("UE7 Printemps").ects(2.5).workloadHours(25).obligation(false).build()
        );
        teachingUnitRepository.saveAll(ues);

        Map<String, TeachingUnit> ueMap = new HashMap<>();
        ues.forEach(ue -> ueMap.put(ue.getCode(), ue));

        // 2. Créer les semestrial knowledge blocks
        SemestrialKnowledgeBlock bcc1s1 = SemestrialKnowledgeBlock.builder()
                .code("BCC1S1")
                .label("BCC1 Semestre 1")
                .semester(1)
                .ects(26)
                .unitsCoefficientAssociation(Map.of(
                        ueMap.get("UE1"), 7.0,
                        ueMap.get("UE2"), 6.0,
                        ueMap.get("UE3"), 2.0,
                        ueMap.get("UE4"), 6.0
                ))
                .build();

        SemestrialKnowledgeBlock bcc1s2 = SemestrialKnowledgeBlock.builder()
                .code("BCC1S2")
                .label("BCC1 Semestre 2")
                .semester(2)
                .ects(9)
                .unitsCoefficientAssociation(Map.of(
                        ueMap.get("UE5"), 4.0
                ))
                .build();

        SemestrialKnowledgeBlock bcc2s1 = SemestrialKnowledgeBlock.builder()
                .code("BCC2S1")
                .label("BCC2 Semestre 1")
                .semester(1)
                .ects(12)
                .unitsCoefficientAssociation(Map.of(
                        ueMap.get("UE1"), 6.0,
                        ueMap.get("UE2"), 7.0,
                        ueMap.get("UE3"), 2.0
                ))
                .build();

        SemestrialKnowledgeBlock bcc2s2 = SemestrialKnowledgeBlock.builder()
                .code("BCC2S2")
                .label("BCC2 Semestre 2")
                .semester(2)
                .ects(13)
                .unitsCoefficientAssociation(Map.of(
                        ueMap.get("UE6"), 6.0,
                        ueMap.get("UE5"), 5.0,
                        ueMap.get("UE7"), 2.5
                ))
                .build();

        // 3. Regrouper dans BCC annuel
        AnnualKnowledgeBlock bcc1 = AnnualKnowledgeBlock.builder()
                .id("BCC1")
                .semesters(List.of(bcc1s1, bcc1s2))
                .build();

        AnnualKnowledgeBlock bcc2 = AnnualKnowledgeBlock.builder()
                .id("BCC2")
                .semesters(List.of(bcc2s1, bcc2s2))
                .build();

        // 4. Créer le curriculum plan
        CurriculumPlan plan = CurriculumPlan.builder()
                .id(1L)
                .academicYear("2024-2025")
                .name("M1 Informatique")
                .annualKnowledgeBlocks(List.of(bcc1, bcc2))
                .build();
        curriculumPlanRepository.save(plan);

        // 5. Générer les IP avec notes
        Random random = new Random();
        List<PedagogicalRegistration> registrations = new ArrayList<>();

        for (Student student : studentRepository.findAll()) {
            for (TeachingUnit ue : ues) {
                // Option : inclure UNE seule UE optionnelle par semestre
                if (!ue.isObligation()) {
                    if ((ue.getCode().equals("UE3") && random.nextBoolean()) ||
                            (ue.getCode().equals("UE6") && random.nextBoolean()) ||
                            (ue.getCode().equals("UE7") && random.nextBoolean())) {
                        // Probabilité de choisir UNE optionnelle
                    } else {
                        continue;
                    }
                }
                registrations.add(PedagogicalRegistration.builder()
                        .student(student)
                        .teachingUnit(ue)
                        .semester(ue.getCode().equals("UE5") || ue.getCode().startsWith("UE6") || ue.getCode().equals("UE7") ? 2 : 1)
                        .grade(Math.round((8 + random.nextDouble() * 10) * 10.0) / 10.0)
                        .build());
            }
        }

        registrationRepository.saveAll(registrations);
        System.out.println("Étudiants présents avant attribution : " + studentRepository.count());
        System.out.println("Curriculum et IP générés pour tous les étudiants.");
    }
}