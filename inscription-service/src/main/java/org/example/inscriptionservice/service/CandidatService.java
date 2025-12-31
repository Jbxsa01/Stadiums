package org.example.inscriptionservice.service;

import org.example.inscriptionservice.model.Candidat;
import org.example.inscriptionservice.repository.CandidatRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CandidatService {
    private final CandidatRepository candidatRepository;
    public CandidatService(CandidatRepository candidatRepository) {
        this.candidatRepository = candidatRepository;
    }
    public List<Candidat> getAll() { return candidatRepository.findAll(); }
    public Candidat save(Candidat candidat) { return candidatRepository.save(candidat); }
    // ...autres méthodes nécessaires
}
