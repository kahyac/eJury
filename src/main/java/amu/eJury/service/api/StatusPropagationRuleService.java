package amu.eJury.service.api;

import amu.eJury.model.result.ExceptionalStatus;

/** priorité des statuts (ABI ≻ ABJ ≻ AR ≻ NONE) */
public interface StatusPropagationRuleService {
    ExceptionalStatus propagate(ExceptionalStatus a, ExceptionalStatus b);
}