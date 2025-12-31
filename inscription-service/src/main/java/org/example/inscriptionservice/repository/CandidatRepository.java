package org.example.inscriptionservice.repository;

import org.example.inscriptionservice.model.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatRepository extends JpaRepository<Candidat, Long> {
}
