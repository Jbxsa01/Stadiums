package org.example.soutenanceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoutenanceDTO {
    private Long id;
    private Long doctorantId;
    private String nomDoctorant;
    private String prenomDoctorant;
    private String emailDoctorant;
    private LocalDateTime dateSoutenance;
    private String lieu;
    private String statut;
    private Integer nombrePublications;
    private Integer creditsFormation;
    private Boolean checklistValidee;
    private List<JuryDTO> jury;
    private List<RapportDTO> rapports;
}
