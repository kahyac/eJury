package amu.jury_m1.dao;

import amu.jury_m1.domain.pedagogy.CurriculumPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurriculumPlanRepository extends JpaRepository<CurriculumPlan, String> {}