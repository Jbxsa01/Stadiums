package org.example.soutenanceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapportDTO {
    private Long id;
    private String avis; // FAVORABLE, DEFAVORABLE, RESERVE
    private String commentaire;
    private String fichierUrl;
    private Long membreJuryId;
    private String nomMembre;
}
