package amu.eJury.service.adapter;

import amu.eJury.dao.SemestrialBlockResultRepository;
import amu.eJury.model.result.SemestrialBlockResult;
import amu.eJury.service.port.SaveSemestrialResultPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class SemestrialResultJpaAdapter implements SaveSemestrialResultPort {

    private final SemestrialBlockResultRepository semestrialBlockResultRepository;

    @Override
    public SemestrialBlockResult save(SemestrialBlockResult result) {
        return semestrialBlockResultRepository.save(result);
    }
}