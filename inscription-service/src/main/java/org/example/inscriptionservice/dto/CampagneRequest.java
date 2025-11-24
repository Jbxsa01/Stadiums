package org.example.inscriptionservice.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class CampagneRequest {
    @NotBlank
    private String titre;

    private String description;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;
}
