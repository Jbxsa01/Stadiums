package org.example.inscriptionservice.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ValidationRequest {
    @NotNull
    private Boolean valider;

    @Size(max = 500)
    private String commentaire;
}
