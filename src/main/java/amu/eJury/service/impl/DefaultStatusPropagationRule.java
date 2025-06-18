package amu.eJury.service.impl;

/* ----------------------------------------------------
 * 1.2  StatusPropagationRule — priorité ABI ≻ ABJ ≻ AR
 * ---------------------------------------------------- */

import amu.eJury.model.result.ExceptionalStatus;
import amu.eJury.service.api.StatusPropagationRule;
import org.springframework.stereotype.Component;

@Component
public class DefaultStatusPropagationRule implements StatusPropagationRule {

    @Override
    public ExceptionalStatus propagate(ExceptionalStatus a, ExceptionalStatus b) {
        if (a == ExceptionalStatus.ABI || b == ExceptionalStatus.ABI) return ExceptionalStatus.ABI;
        if (a == ExceptionalStatus.ABJ || b == ExceptionalStatus.ABJ) return ExceptionalStatus.ABJ;
        if (a == ExceptionalStatus.AR  || b == ExceptionalStatus.AR)  return ExceptionalStatus.AR;
        return ExceptionalStatus.NONE;
    }
}