package amu.eJury.service.impl;

/* ----------------------------------------------------
 * 1.1  BonusPolicy — aucune bonification par défaut
 * ---------------------------------------------------- */

import amu.eJury.model.student.Student;
import amu.eJury.service.api.BonusPolicy;
import org.springframework.stereotype.Component;

@Component
public class NoBonusPolicy implements BonusPolicy {

    @Override
    public double bonusFor(Student s, int semester) {
        return 0.0;   // stratégie « pas de bonus »
    }
}