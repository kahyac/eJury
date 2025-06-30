package amu.eJury.service.dtos;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class StudentRegistrationsViewDTO {
    private String studentId;
    private String studentFullName;
    private List<UeView> ues;

    private Double bonusS1; // BONUS SEMESTRE 1
    private Double bonusS2; // BONUS SEMESTRE 2

    public StudentRegistrationsViewDTO(String studentId, String studentFullName, List<UeView> ues,
                                       Double bonusS1, Double bonusS2) {
        this.studentId = studentId;
        this.studentFullName = studentFullName;
        this.ues = ues;
        this.bonusS1 = bonusS1;
        this.bonusS2 = bonusS2;
    }

    @Setter
    @Getter
    public static class UeView {
        private String label;
        private boolean isOptional;
        private Long unitId;

        public UeView(String label, boolean isOptional, Long unitId) {
            this.label = label;
            this.isOptional = isOptional;
            this.unitId = unitId;
        }
    }
}
