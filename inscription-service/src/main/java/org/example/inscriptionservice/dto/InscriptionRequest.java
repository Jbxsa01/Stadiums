package org.example.inscriptionservice.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class InscriptionRequest {
    @NotNull
    private Long campagneId;

    @NotNull
    private Long doctorantId;

    @NotNull
    private Long directeurId;

    @NotBlank
    @Size(min = 10, max = 255)
    private String sujetThese;

    @NotBlank
    @Size(min = 50, max = 2000)
    private String descriptionProjet;

    @NotBlank
    private String domaineRecherche;
}