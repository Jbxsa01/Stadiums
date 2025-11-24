package org.example.inscriptionservice.controller;

import org.example.inscriptionservice.dto.ValidationRequest;
import org.example.inscriptionservice.entity.Inscription;
import org.example.inscriptionservice.service.InscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/inscription/inscriptions")
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionService inscriptionService;

    @PostMapping
    public ResponseEntity<Inscription> createInscription(@Valid @RequestBody Inscription inscription) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inscriptionService.createInscription(inscription));
    }

    @PostMapping("/{id}/soumettre")
    public ResponseEntity<Inscription> soumettreInscription(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionService.soumettreInscription(id));
    }

    @PostMapping("/{id}/valider-directeur")
    public ResponseEntity<Inscription> validerParDirecteur(
            @PathVariable Long id,
            @Valid @RequestBody ValidationRequest request) {
        return ResponseEntity.ok(inscriptionService.validerParDirecteur(
                id, request.getValider(), request.getCommentaire()));
    }

    @PostMapping("/{id}/valider-admin")
    public ResponseEntity<Inscription> validerParAdmin(
            @PathVariable Long id,
            @Valid @RequestBody ValidationRequest request) {
        return ResponseEntity.ok(inscriptionService.validerParAdmin(
                id, request.getValider(), request.getCommentaire()));
    }

    @GetMapping
    public ResponseEntity<List<Inscription>> getAllInscriptions() {
        return ResponseEntity.ok(inscriptionService.getAllInscriptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inscription> getInscriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionService.getInscriptionById(id));
    }

    @GetMapping("/doctorant/{doctorantId}")
    public ResponseEntity<List<Inscription>> getInscriptionsByDoctorant(@PathVariable Long doctorantId) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsByDoctorant(doctorantId));
    }

    @GetMapping("/directeur/{directeurId}")
    public ResponseEntity<List<Inscription>> getInscriptionsByDirecteur(@PathVariable Long directeurId) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsByDirecteur(directeurId));
    }

    @GetMapping("/campagne/{campagneId}")
    public ResponseEntity<List<Inscription>> getInscriptionsByCampagne(@PathVariable Long campagneId) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsByCampagne(campagneId));
    }

    @GetMapping("/attente-directeur")
    public ResponseEntity<List<Inscription>> getInscriptionsEnAttenteDirecteur() {
        return ResponseEntity.ok(inscriptionService.getInscriptionsEnAttenteDirecteur());
    }

    @GetMapping("/attente-admin")
    public ResponseEntity<List<Inscription>> getInscriptionsEnAttenteAdmin() {
        return ResponseEntity.ok(inscriptionService.getInscriptionsEnAttenteAdmin());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inscription> updateInscription(
            @PathVariable Long id,
            @Valid @RequestBody Inscription inscription) {
        return ResponseEntity.ok(inscriptionService.updateInscription(id, inscription));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInscription(@PathVariable Long id) {
        inscriptionService.deleteInscription(id);
        return ResponseEntity.noContent().build();
    }
}