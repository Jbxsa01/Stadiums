package org.example.inscriptionservice.service;

import org.example.inscriptionservice.model.Candidat;
import org.example.inscriptionservice.model.Publication;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class RegleGestionService {
    public boolean peutSeReinscrire(LocalDate datePremiereInscription, boolean derogation) {
        if (derogation) return true;
        return datePremiereInscription.plusYears(3).isAfter(LocalDate.now());
    }

    public boolean depasseDureeMax(LocalDate datePremiereInscription) {
        return datePremiereInscription.plusYears(6).isBefore(LocalDate.now());
    }

    public boolean preRequisSoutenance(Candidat candidat) {
        long articles = candidat.getPublications().stream()
            .filter(p -> "JOURNAL".equals(p.getType()) && ("Q1".equals(p.getRang()) || "Q2".equals(p.getRang())))
            .count();
        long conferences = candidat.getPublications().stream()
            .filter(p -> "CONFERENCE".equals(p.getType()))
            .count();
        int heuresFormation = candidat.getFormations().stream().mapToInt(f -> f.getHeures()).sum();
        return articles >= 2 && conferences >= 2 && heuresFormation >= 200;
    }
}
