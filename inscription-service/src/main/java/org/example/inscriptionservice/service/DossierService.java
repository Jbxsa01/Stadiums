package org.example.inscriptionservice.service;

import org.example.inscriptionservice.model.Dossier;
import org.example.inscriptionservice.repository.DossierRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DossierService {
    private final DossierRepository dossierRepository;
    public DossierService(DossierRepository dossierRepository) {
        this.dossierRepository = dossierRepository;
    }
    public List<Dossier> getAll() { return dossierRepository.findAll(); }
    public Dossier save(Dossier dossier) { return dossierRepository.save(dossier); }
    // ...autres méthodes nécessaires
}
