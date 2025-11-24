package org.example.inscriptionservice.controller;

import org.example.inscriptionservice.entity.Campagne;
import org.example.inscriptionservice.service.CampagneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/inscription/campagnes")
@RequiredArgsConstructor
public class CampagneController {

    private final CampagneService campagneService;

    @PostMapping
    public ResponseEntity<Campagne> createCampagne(@RequestBody Campagne campagne) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(campagneService.createCampagne(campagne));
    }

    @GetMapping
    public ResponseEntity<List<Campagne>> getAllCampagnes() {
        return ResponseEntity.ok(campagneService.getAllCampagnes());
    }

    @GetMapping("/ouvertes")
    public ResponseEntity<List<Campagne>> getCampagnesOuvertes() {
        return ResponseEntity.ok(campagneService.getCampagnesOuvertes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campagne> getCampagneById(@PathVariable Long id) {
        return ResponseEntity.ok(campagneService.getCampagneById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Campagne> updateCampagne(
            @PathVariable Long id,
            @RequestBody Campagne campagne) {
        return ResponseEntity.ok(campagneService.updateCampagne(id, campagne));
    }

    @PutMapping("/{id}/fermer")
    public ResponseEntity<Campagne> fermerCampagne(@PathVariable Long id) {
        return ResponseEntity.ok(campagneService.fermerCampagne(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampagne(@PathVariable Long id) {
        campagneService.deleteCampagne(id);
        return ResponseEntity.noContent().build();
    }
}