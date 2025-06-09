package amu.jury_m1.dao;

import amu.jury_m1.domain.pedagogy.UnitInBlockAssociation;
import amu.jury_m1.domain.pedagogy.UnitInBlockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitInBlockAssociationRepository extends JpaRepository<UnitInBlockAssociation, UnitInBlockId> {
}
