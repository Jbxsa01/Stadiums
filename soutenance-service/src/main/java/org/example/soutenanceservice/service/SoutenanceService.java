package org.example.soutenanceservice.service;

import org.example.soutenanceservice.client.UserClient;
import org.example.soutenanceservice.entity.Soutenance;
import org.example.soutenanceservice.entity.Jury;
import org.example.soutenanceservice.entity.Rapport;
import org.example.soutenanceservice.repository.SoutenanceRepository;
import org.example.soutenanceservice.repository.JuryRepository;
import org.example.soutenanceservice.repository.RapportRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
@RequiredArgsConstructor
public class SoutenanceService {
    private final SoutenanceRepository soutenanceRepository;
    private final JuryRepository juryRepository;
    private final RapportRepository rapportRepository;
    private final UserClient userClient;

    public Soutenance createSoutenance(Soutenance soutenance) {
        return soutenanceRepository.save(soutenance);
    }

    public List<Soutenance> getAllSoutenances() {
        return soutenanceRepository.findAll();
    }

    public Optional<Soutenance> getSoutenanceById(Long id) {
        return soutenanceRepository.findById(id);
    }

    public void deleteSoutenance(Long id) {
        soutenanceRepository.deleteById(id);
    }

    // Jury
    public Jury addJury(Jury jury) {
        // Vérifier que le membre du jury existe et a un rôle compatible via users-service
        checkMembreJury(jury.getMembreId());
        return juryRepository.save(jury);
    }

    public List<Jury> getAllJury() {
        return juryRepository.findAll();
    }

    public List<Jury> getJuryBySoutenance(Long soutenanceId) {
        return juryRepository.findBySoutenanceId(soutenanceId);
    }

    public void deleteJury(Long id) {
        juryRepository.deleteById(id);
    }

    // Rapport
    public Rapport addRapport(Rapport rapport) {
        return rapportRepository.save(rapport);
    }

    public List<Rapport> getAllRapports() {
        return rapportRepository.findAll();
    }

    // Vérification du membre du jury via users-service, protégée par Resilience4j
    @CircuitBreaker(name = "user-service", fallbackMethod = "checkMembreJuryFallback")
    @Retry(name = "user-service")
    public void checkMembreJury(Long membreId) {
        if (membreId == null) {
            throw new IllegalArgumentException("L'id du membre du jury est obligatoire");
        }
        UserClient.UserDto user = userClient.getUserById(membreId);
        if (user == null) {
            throw new IllegalStateException("Aucun utilisateur trouvé pour l'id " + membreId);
        }
        // Ici, selon le PDF, tu peux éventuellement vérifier le rôle (ENSEIGNANT, EXTERNE, etc.)
    }

    public void checkMembreJuryFallback(Long membreId, Throwable t) {
        throw new RuntimeException(
                "Service utilisateur indisponible pour vérifier le membre du jury " + membreId + " : " + t.getMessage(),
                t
        );
    }
}
