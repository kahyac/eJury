package amu.jury_m1.domain.pedagogy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * KnowledgeBlock — Represents a BCC (Bloc de Connaissances et Compétences).
 */
@Getter
@RequiredArgsConstructor
public class KnowledgeBlock {
    private final String code;         // Code unique du bloc
    private final String label;        // Intitulé du bloc
    private final int semester;        // 1 ou 2
}
