package org.example.soutenanceservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.soutenanceservice.entity.Soutenance;
import org.example.soutenanceservice.entity.Jury;
import org.example.soutenanceservice.entity.Rapport;
import org.example.soutenanceservice.service.SoutenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/soutenances")
@RequiredArgsConstructor
public class SoutenanceController {
    private final SoutenanceService soutenanceService;

    @PostMapping
    public ResponseEntity<Soutenance> createSoutenance(@RequestBody Soutenance soutenance) {
        return ResponseEntity.ok(soutenanceService.createSoutenance(soutenance));
    }

    @GetMapping
    public ResponseEntity<List<Soutenance>> getAllSoutenances() {
        return ResponseEntity.ok(soutenanceService.getAllSoutenances());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Soutenance> getSoutenanceById(@PathVariable Long id) {
        return soutenanceService.getSoutenanceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoutenance(@PathVariable Long id) {
        soutenanceService.deleteSoutenance(id);
        return ResponseEntity.noContent().build();
    }

    // Jury
    @PostMapping("/{soutenanceId}/jury")
    public ResponseEntity<Jury> addJury(@PathVariable Long soutenanceId, @RequestBody Jury jury) {
        // Associer la soutenance
        jury.setSoutenance(new Soutenance());
        jury.getSoutenance().setId(soutenanceId);
        return ResponseEntity.ok(soutenanceService.addJury(jury));
    }

    @GetMapping("/jury")
    public ResponseEntity<List<Jury>> getAllJury() {
        return ResponseEntity.ok(soutenanceService.getAllJury());
    }

    @GetMapping("/{soutenanceId}/jury")
    public ResponseEntity<List<Jury>> getJuryBySoutenance(@PathVariable Long soutenanceId) {
        return ResponseEntity.ok(soutenanceService.getJuryBySoutenance(soutenanceId));
    }

    @DeleteMapping("/jury/{juryId}")
    public ResponseEntity<Void> deleteJury(@PathVariable Long juryId) {
        soutenanceService.deleteJury(juryId);
        return ResponseEntity.noContent().build();
    }

    // Rapport
    @PostMapping("/{soutenanceId}/rapport")
    public ResponseEntity<Rapport> addRapport(@PathVariable Long soutenanceId, @RequestBody Rapport rapport) {
        rapport.setSoutenance(new Soutenance());
        rapport.getSoutenance().setId(soutenanceId);
        return ResponseEntity.ok(soutenanceService.addRapport(rapport));
    }

    @GetMapping("/rapport")
    public ResponseEntity<List<Rapport>> getAllRapports() {
        return ResponseEntity.ok(soutenanceService.getAllRapports());
    }
}
