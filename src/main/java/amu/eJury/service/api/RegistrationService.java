package amu.eJury.service.api;

import amu.eJury.model.users.Student;

import java.util.Map;

public interface RegistrationService {
    void deleteRegistration(Long studentId, Long unitId);
    public Map<Student, Integer> checkECTSCompletenessForSemester(int semester);

}
