package org.example.inscriptionservice.controller;

import org.example.inscriptionservice.model.Campagne;
import org.example.inscriptionservice.model.Dossier;
import org.example.inscriptionservice.model.Candidat;
import org.example.inscriptionservice.service.CampagneService;
import org.example.inscriptionservice.service.DossierService;
import org.example.inscriptionservice.service.CandidatService;
import org.example.inscriptionservice.service.RegleGestionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/inscription")
public class InscriptionController {
    private final CampagneService campagneService;
    private final DossierService dossierService;
    private final CandidatService candidatService;

    private final RegleGestionService regleGestionService;

    public InscriptionController(CampagneService campagneService, DossierService dossierService, CandidatService candidatService, RegleGestionService regleGestionService) {
        this.campagneService = campagneService;
        this.dossierService = dossierService;
        this.candidatService = candidatService;
        this.regleGestionService = regleGestionService;
    }
    // Exemple : Vérifier si le doctorant peut se réinscrire
    @GetMapping("/candidats/{id}/peut-se-reinscrire")
    public boolean peutSeReinscrire(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean derogation) {
        var candidat = candidatService.getAll().stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
        if (candidat == null) return false;
        // Supposons que la date de première inscription est stockée dans le premier dossier
        var dossier = candidat.getDossiers().isEmpty() ? null : candidat.getDossiers().get(0);
        if (dossier == null) return false;
        // Remplacer par la vraie date d'inscription
        java.time.LocalDate dateInscription = java.time.LocalDate.now().minusYears(2); // exemple
        return regleGestionService.peutSeReinscrire(dateInscription, derogation);
    }

    // Exemple : Vérifier si le doctorant a atteint la durée maximale
    @GetMapping("/candidats/{id}/alerte-duree-max")
    public boolean alerteDureeMax(@PathVariable Long id) {
        var candidat = candidatService.getAll().stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
        if (candidat == null) return false;
        var dossier = candidat.getDossiers().isEmpty() ? null : candidat.getDossiers().get(0);
        if (dossier == null) return false;
        java.time.LocalDate dateInscription = java.time.LocalDate.now().minusYears(6); // exemple
        return regleGestionService.depasseDureeMax(dateInscription);
    }

    // Exemple : Vérifier les prérequis à la soutenance
    @GetMapping("/candidats/{id}/pre-requis-soutenance")
    public boolean preRequisSoutenance(@PathVariable Long id) {
        var candidat = candidatService.getAll().stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
        if (candidat == null) return false;
        return regleGestionService.preRequisSoutenance(candidat);
    }

    @GetMapping("/campagnes")
    public List<Campagne> getCampagnes() { return campagneService.getAll(); }

    @PostMapping("/campagnes")
    public Campagne createCampagne(@RequestBody Campagne campagne) { return campagneService.save(campagne); }

    @GetMapping("/dossiers")
    public List<Dossier> getDossiers() { return dossierService.getAll(); }

    @PostMapping("/dossiers")
    public Dossier createDossier(@RequestBody Dossier dossier) { return dossierService.save(dossier); }

    @PostMapping("/dossiers/{id}/pieces")
    public String uploadPiece(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // TODO: Vérifier format et stocker le fichier
        return "Fichier reçu: " + file.getOriginalFilename();
    }

    // ...autres endpoints pour le circuit de validation, notifications, etc.
}
