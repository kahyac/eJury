package amu.jury_m1.service.port;

import amu.jury_m1.model.result.SemestrialBlockResult;

public interface SaveSemestrialResultPort {
    SemestrialBlockResult save(SemestrialBlockResult r);
}
