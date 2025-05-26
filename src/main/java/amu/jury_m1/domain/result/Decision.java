package amu.jury_m1.domain.result;

public enum Decision {
    PASSED,             // ADM
    FAILED,             // Aj
    ABSENCE_JUSTIFIED,  // ABJ
    ABSENCE_UNJUSTIFIED,// ABI
    NO_RESULT           // AR : Absence de résultat
}