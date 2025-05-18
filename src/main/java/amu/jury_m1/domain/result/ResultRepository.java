package amu.jury_m1.domain.result;

import amu.jury_m1.domain.student.StudentId;

import java.time.Year;
import java.util.List;

public interface ResultRepository {
    List<BccAnnualResult> findByStudentAndYear(StudentId id, Year year);
    void saveAll(List<BccAnnualResult> results);
}