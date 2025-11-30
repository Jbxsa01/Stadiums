package org.example.soutenanceservice.repository;

import org.example.soutenanceservice.entity.Jury;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JuryRepository extends JpaRepository<Jury, Long> {
    List<Jury> findBySoutenanceId(Long soutenanceId);
}
