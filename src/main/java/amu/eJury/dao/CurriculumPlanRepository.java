package amu.eJury.dao;

import amu.eJury.model.pedagogy.CurriculumPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurriculumPlanRepository extends JpaRepository<CurriculumPlan, Long> {}