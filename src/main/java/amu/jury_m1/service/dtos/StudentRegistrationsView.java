package amu.jury_m1.service.dtos;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class StudentRegistrationsView {
    private String studentId;
    private String studentFullName;
    private List<UeView> ues;

    public StudentRegistrationsView(String studentId, String studentFullName, List<UeView> ues) {
        this.studentId = studentId;
        this.studentFullName = studentFullName;
        this.ues = ues;
    }

    @Setter
    @Getter
    public static class UeView {

        private String label;
        private boolean isOptional;

        public UeView(String label, boolean isOptional) {
            this.label = label;
            this.isOptional = isOptional;
        }
    }
}
