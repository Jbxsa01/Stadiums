package org.example.inscriptionservice.repository;

import org.example.inscriptionservice.entity.Inscription;
import org.example.inscriptionservice.enums.StatutInscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findByDoctorantId(Long doctorantId);
    List<Inscription> findByDirecteurId(Long directeurId);
    List<Inscription> findByCampagneId(Long campagneId);
    List<Inscription> findByStatut(StatutInscription statut);
}
