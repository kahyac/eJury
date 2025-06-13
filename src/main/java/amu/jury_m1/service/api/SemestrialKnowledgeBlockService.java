package amu.jury_m1.service.api;

import amu.jury_m1.service.dtos.SemestrialKnowledgeBlockDto;

public interface SemestrialKnowledgeBlockService {
    void addSemestrialBlockToAnnual(String annualBlockId, SemestrialKnowledgeBlockDto dto);
    void associateTeachingUnitToSemestrialBlock(String blockCode, String unitCode, double coefficient);
    String findAnnualIdBySemBlockCode(String blockCode);
}
