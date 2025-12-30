package org.example.soutenanceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JuryDTO {
    private Long id;
    private Long membreId;
    private String nomComplet;
    private String etablissement;
    private String role; // PRESIDENT, RAPPORTEUR, EXAMINATEUR, INVITE
    private Integer ordre;
}
