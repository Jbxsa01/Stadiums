package org.example.inscriptionservice.repository;

import org.example.inscriptionservice.entity.Campagne;
import org.example.inscriptionservice.enums.StatutCampagne;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CampagneRepository extends JpaRepository<Campagne, Long> {
    List<Campagne> findByStatut(StatutCampagne statut);
}
