package amu.jury_m1.domain.pedagogy;

import jakarta.persistence.*;
import lombok.*;

/**
 * UnitInBlockAssociation — Représente l’association entre une UE et un BCC,
 * avec un coefficient défini dans le cadre d’une maquette pédagogique.
 */
@Entity
@Table(name = "unit_in_block")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitInBlockAssociation {

    /**
     * Clé composite composée du code de l'UE et du code du BCC.
     * Elle est définie par la classe UnitInBlockId (annotée @Embeddable).
     */
    @EmbeddedId
    private UnitInBlockId id;

    /**
     * Coefficient de l’UE dans ce bloc (défini dans la maquette).
     */
    private Double coefficient;
}
