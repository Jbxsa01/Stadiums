package org.example.soutenanceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeSoutenanceDTO {
    private Long doctorantId;
    private Integer nombrePublications;
    private Integer creditsFormation;
    private String demandeManuscrite;
    private String rapportThese;
    private String rapportAntiPlagiat;
    private String rapportPublications;
    private String attestationsFormations;
}
