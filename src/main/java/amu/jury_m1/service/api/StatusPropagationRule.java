package amu.jury_m1.service.api;

import amu.jury_m1.model.result.ExceptionalStatus;

/** priorité des statuts (ABI ≻ ABJ ≻ AR ≻ NONE) */
public interface StatusPropagationRule {
    ExceptionalStatus propagate(ExceptionalStatus a, ExceptionalStatus b);
}