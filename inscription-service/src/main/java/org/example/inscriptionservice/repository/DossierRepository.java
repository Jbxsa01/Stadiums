package org.example.inscriptionservice.repository;

import org.example.inscriptionservice.model.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DossierRepository extends JpaRepository<Dossier, Long> {
}
