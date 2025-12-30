package org.example.soutenanceservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.soutenanceservice.dto.*;
import org.example.soutenanceservice.service.SoutenanceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/soutenances")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SoutenanceController {
    private final SoutenanceService soutenanceService;

    // ========== DEMANDES DE SOUTENANCE ==========

    /**
     * Créer une nouvelle demande de soutenance
     */
    @PostMapping("/demandes")
    public ResponseEntity<SoutenanceDTO> creerDemande(@RequestBody DemandeSoutenanceDTO demande) {
        try {
            SoutenanceDTO created = soutenanceService.creerDemandeSoutenance(demande);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Valider la check-list de prérequis
     */
    @PutMapping("/{id}/valider-checklist")
    public ResponseEntity<SoutenanceDTO> validerChecklist(
            @PathVariable Long id,
            @RequestParam Long adminId) {
        try {
            SoutenanceDTO validated = soutenanceService.validerChecklist(id, adminId);
            return ResponseEntity.ok(validated);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== GESTION DU JURY ==========

    /**
     * Proposer la composition complète du jury
     */
    @PostMapping("/{id}/jury/proposer")
    public ResponseEntity<SoutenanceDTO> proposerJury(
            @PathVariable Long id,
            @RequestBody List<JuryDTO> membresJury) {
        try {
            SoutenanceDTO updated = soutenanceService.proposerJury(id, membresJury);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Ajouter un membre au jury
     */
    @PostMapping("/{soutenanceId}/jury")
    public ResponseEntity<JuryDTO> ajouterMembreJury(
            @PathVariable Long soutenanceId,
            @RequestBody JuryDTO juryDTO) {
        try {
            JuryDTO created = soutenanceService.ajouterMembreJury(soutenanceId, juryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Supprimer un membre du jury
     */
    @DeleteMapping("/jury/{juryId}")
    public ResponseEntity<Void> supprimerMembreJury(@PathVariable Long juryId) {
        soutenanceService.deleteJury(juryId);
        return ResponseEntity.noContent().build();
    }

    // ========== GESTION DES RAPPORTS ==========

    /**
     * Soumettre un rapport
     */
    @PostMapping("/{soutenanceId}/rapports")
    public ResponseEntity<RapportDTO> soumettreRapport(
            @PathVariable Long soutenanceId,
            @RequestBody RapportDTO rapportDTO) {
        try {
            RapportDTO created = soutenanceService.soumettreRapport(soutenanceId, rapportDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Vérifier si tous les rapports sont favorables
     */
    @GetMapping("/{id}/rapports/favorables")
    public ResponseEntity<Boolean> verifierRapportsFavorables(@PathVariable Long id) {
        boolean favorables = soutenanceService.tousRapportsFavorables(id);
        return ResponseEntity.ok(favorables);
    }

    // ========== AUTORISATION ET PLANIFICATION ==========

    /**
     * Autoriser et planifier la soutenance
     */
    @PutMapping("/{id}/autoriser")
    public ResponseEntity<SoutenanceDTO> autoriserEtPlanifier(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam String lieu,
            @RequestParam Long adminId) {
        try {
            SoutenanceDTO authorized = soutenanceService.autoriserEtPlanifier(id, date, lieu, adminId);
            return ResponseEntity.ok(authorized);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== CONSULTATION ==========

    /**
     * Récupérer toutes les soutenances
     */
    @GetMapping
    public ResponseEntity<List<SoutenanceDTO>> getAllSoutenances() {
        List<SoutenanceDTO> soutenances = soutenanceService.getAllSoutenances();
        return ResponseEntity.ok(soutenances);
    }

    /**
     * Récupérer une soutenance par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SoutenanceDTO> getSoutenanceById(@PathVariable Long id) {
        return soutenanceService.getSoutenanceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupérer les soutenances d'un doctorant
     */
    @GetMapping("/doctorant/{doctorantId}")
    public ResponseEntity<List<SoutenanceDTO>> getSoutenancesByDoctorant(
            @PathVariable Long doctorantId) {
        List<SoutenanceDTO> soutenances = soutenanceService.getSoutenancesByDoctorant(doctorantId);
        return ResponseEntity.ok(soutenances);
    }

    /**
     * Récupérer les soutenances par statut
     */
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<SoutenanceDTO>> getSoutenancesByStatut(
            @PathVariable String statut) {
        List<SoutenanceDTO> soutenances = soutenanceService.getSoutenancesByStatut(statut);
        return ResponseEntity.ok(soutenances);
    }

    /**
     * Supprimer une soutenance
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoutenance(@PathVariable Long id) {
        soutenanceService.deleteSoutenance(id);
        return ResponseEntity.noContent().build();
    }
}
