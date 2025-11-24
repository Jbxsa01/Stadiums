package org.example.inscriptionservice.service;

import org.example.inscriptionservice.entity.Campagne;
import org.example.inscriptionservice.enums.StatutCampagne;
import org.example.inscriptionservice.repository.CampagneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampagneService {

    private final CampagneRepository campagneRepo;

    public Campagne createCampagne(Campagne campagne) {
        campagne.setStatut(StatutCampagne.OUVERTE);
        campagne.setCreatedAt(LocalDateTime.now());
        return campagneRepo.save(campagne);
    }

    public List<Campagne> getAllCampagnes() {
        return campagneRepo.findAll();
    }

    public List<Campagne> getCampagnesOuvertes() {
        return campagneRepo.findByStatut(StatutCampagne.OUVERTE);
    }

    public Campagne getCampagneById(Long id) {
        return campagneRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Campagne non trouvée"));
    }

    public Campagne updateCampagne(Long id, Campagne campagne) {
        Campagne existing = getCampagneById(id);
        existing.setTitre(campagne.getTitre());
        existing.setDescription(campagne.getDescription());
        existing.setDateDebut(campagne.getDateDebut());
        existing.setDateFin(campagne.getDateFin());
        existing.setUpdatedAt(LocalDateTime.now());
        return campagneRepo.save(existing);
    }

    public Campagne fermerCampagne(Long id) {
        Campagne campagne = getCampagneById(id);
        campagne.setStatut(StatutCampagne.FERMEE);
        campagne.setUpdatedAt(LocalDateTime.now());
        return campagneRepo.save(campagne);
    }

    public void deleteCampagne(Long id) {
        campagneRepo.deleteById(id);
    }
}