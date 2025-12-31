package org.example.inscriptionservice.repository;

import org.example.inscriptionservice.model.Campagne;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampagneRepository extends JpaRepository<Campagne, Long> {
}
