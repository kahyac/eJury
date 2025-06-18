package amu.eJury.service.port;

import amu.eJury.model.result.SemestrialBlockResult;

public interface SaveSemestrialResultPort {
    SemestrialBlockResult save(SemestrialBlockResult r);
}
