package org.example.inscriptionservice.service;

import org.example.inscriptionservice.model.Campagne;
import org.example.inscriptionservice.repository.CampagneRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CampagneService {
    private final CampagneRepository campagneRepository;
    public CampagneService(CampagneRepository campagneRepository) {
        this.campagneRepository = campagneRepository;
    }
    public List<Campagne> getAll() { return campagneRepository.findAll(); }
    public Campagne save(Campagne campagne) { return campagneRepository.save(campagne); }
    // ...autres méthodes nécessaires
}
