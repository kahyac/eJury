package amu.jury_m1.service.adapter;

import amu.jury_m1.dao.SemestrialBlockResultRepository;
import amu.jury_m1.model.result.SemestrialBlockResult;
import amu.jury_m1.service.port.SaveSemestrialResultPort;
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