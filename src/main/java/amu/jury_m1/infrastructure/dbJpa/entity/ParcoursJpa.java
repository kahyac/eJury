/*package amu.jury_m1.infrastructure.dbJpa.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity @Table(name = "parcours")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParcoursJpa {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID idParcours;

    @Column(nullable = false, length = 80)
    private String libelle;

    @Column(nullable = false, length = 80)
    private String responsable;
}*/