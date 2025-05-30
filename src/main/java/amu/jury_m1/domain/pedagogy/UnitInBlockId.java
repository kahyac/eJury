package amu.jury_m1.domain.pedagogy;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * UnitInBlockId — Clé composite représentant une relation (UE ↔ BCC)
 * teachingUnitCode + blockCode forment ensemble la clé primaire.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UnitInBlockId implements Serializable {

    private String teachingUnitCode;
    private String blockCode;

}
