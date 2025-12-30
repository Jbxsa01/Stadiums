package org.example.soutenanceservice.service;

import org.example.soutenanceservice.client.UserClient;
import org.example.soutenanceservice.dto.*;
import org.example.soutenanceservice.entity.Soutenance;
import org.example.soutenanceservice.entity.Jury;
import org.example.soutenanceservice.entity.Rapport;
import org.example.soutenanceservice.repository.SoutenanceRepository;
import org.example.soutenanceservice.repository.JuryRepository;
import org.example.soutenanceservice.repository.RapportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
@RequiredArgsConstructor
@Transactional
public class SoutenanceService {
    private final SoutenanceRepository soutenanceRepository;
    private final JuryRepository juryRepository;
    private final RapportRepository rapportRepository;
    private final UserClient userClient;

    // ========== DEMANDES DE SOUTENANCE ==========

    /**
     * Créer une nouvelle demande de soutenance (initiée par le doctorant)
     */
    public SoutenanceDTO creerDemandeSoutenance(DemandeSoutenanceDTO demande) {
        // Vérifier que le doctorant existe
        checkDoctorant(demande.getDoctorantId());

        Soutenance soutenance = new Soutenance();
        soutenance.setDoctorantId(demande.getDoctorantId());
        soutenance.setStatut("EN_ATTENTE");
        soutenance.setNombrePublications(demande.getNombrePublications());
        soutenance.setCreditsFormation(demande.getCreditsFormation());
        soutenance.setDemandeManuscrite(demande.getDemandeManuscrite());
        soutenance.setRapportThese(demande.getRapportThese());
        soutenance.setRapportAntiPlagiat(demande.getRapportAntiPlagiat());
        soutenance.setRapportPublications(demande.getRapportPublications());
        soutenance.setAttestationsFormations(demande.getAttestationsFormations());

        Soutenance saved = soutenanceRepository.save(soutenance);
        return convertToDTO(saved);
    }

    /**
     * Valider la check-list de prérequis (par l'administration)
     */
    public SoutenanceDTO validerChecklist(Long soutenanceId, Long adminId) {
        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
                .orElseThrow(() -> new IllegalArgumentException("Soutenance non trouvée"));

        // Vérifier les prérequis
        if (soutenance.getNombrePublications() == null || soutenance.getNombrePublications() < 1) {
            throw new IllegalStateException("Nombre de publications insuffisant");
        }
        if (soutenance.getCreditsFormation() == null || soutenance.getCreditsFormation() < 60) {
            throw new IllegalStateException("Crédits de formation insuffisants");
        }

        // Vérifier les documents
        if (soutenance.getDemandeManuscrite() == null ||
                soutenance.getRapportThese() == null ||
                soutenance.getRapportAntiPlagiat() == null) {
            throw new IllegalStateException("Documents manquants");
        }

        soutenance.setChecklistValidee(true);
        soutenance.setDateValidation(LocalDateTime.now());
        soutenance.setValidePar(adminId);
        soutenance.setStatut("VALIDEE");

        return convertToDTO(soutenanceRepository.save(soutenance));
    }

    // ========== GESTION DU JURY ==========

    /**
     * Proposer la composition du jury (par le directeur de thèse)
     */
    public SoutenanceDTO proposerJury(Long soutenanceId, List<JuryDTO> membresJury) {
        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
                .orElseThrow(() -> new IllegalArgumentException("Soutenance non trouvée"));

        if (!"VALIDEE".equals(soutenance.getStatut())) {
            throw new IllegalStateException("La soutenance doit être validée avant de proposer un jury");
        }

        // Supprimer l'ancien jury si existant
        juryRepository.deleteAll(soutenance.getJury());

        // Ajouter les nouveaux membres
        for (JuryDTO dto : membresJury) {
            checkMembreJury(dto.getMembreId());

            Jury membre = new Jury();
            membre.setMembreId(dto.getMembreId());
            membre.setNomComplet(dto.getNomComplet());
            membre.setEtablissement(dto.getEtablissement());
            membre.setRole(dto.getRole());
            membre.setOrdre(dto.getOrdre());
            membre.setSoutenance(soutenance);

            soutenance.getJury().add(membre);
        }

        return convertToDTO(soutenanceRepository.save(soutenance));
    }

    /**
     * Ajouter un membre au jury
     */
    public JuryDTO ajouterMembreJury(Long soutenanceId, JuryDTO juryDTO) {
        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
                .orElseThrow(() -> new IllegalArgumentException("Soutenance non trouvée"));

        checkMembreJury(juryDTO.getMembreId());

        Jury membre = new Jury();
        membre.setMembreId(juryDTO.getMembreId());
        membre.setNomComplet(juryDTO.getNomComplet());
        membre.setEtablissement(juryDTO.getEtablissement());
        membre.setRole(juryDTO.getRole());
        membre.setOrdre(juryDTO.getOrdre());
        membre.setSoutenance(soutenance);

        Jury saved = juryRepository.save(membre);
        return convertJuryToDTO(saved);
    }

    // ========== GESTION DES RAPPORTS ==========

    /**
     * Soumettre un rapport de pré-soutenance
     */
    public RapportDTO soumettreRapport(Long soutenanceId, RapportDTO rapportDTO) {
        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
                .orElseThrow(() -> new IllegalArgumentException("Soutenance non trouvée"));

        Rapport rapport = new Rapport();
        rapport.setAvis(rapportDTO.getAvis());
        rapport.setFichierUrl(rapportDTO.getFichierUrl());
        rapport.setSoutenance(soutenance);

        Rapport saved = rapportRepository.save(rapport);
        return convertRapportToDTO(saved);
    }

    /**
     * Vérifier si tous les rapports sont favorables
     */
    public boolean tousRapportsFavorables(Long soutenanceId) {
        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
                .orElseThrow(() -> new IllegalArgumentException("Soutenance non trouvée"));

        List<Rapport> rapports = soutenance.getRapports();
        if (rapports.isEmpty()) {
            return false;
        }

        return rapports.stream()
                .allMatch(r -> "FAVORABLE".equals(r.getAvis()));
    }

    // ========== AUTORISATION ET PLANIFICATION ==========

    /**
     * Autoriser et planifier la soutenance
     */
    public SoutenanceDTO autoriserEtPlanifier(Long soutenanceId, LocalDateTime date, String lieu, Long adminId) {
        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
                .orElseThrow(() -> new IllegalArgumentException("Soutenance non trouvée"));

        if (!soutenance.getChecklistValidee()) {
            throw new IllegalStateException("La check-list doit être validée");
        }

        if (!tousRapportsFavorables(soutenanceId)) {
            throw new IllegalStateException("Tous les rapports doivent être favorables");
        }

        soutenance.setDateSoutenance(date);
        soutenance.setLieu(lieu);
        soutenance.setStatut("PLANIFIEE");
        soutenance.setAutorisationSoutenance("AUTORISEE_PAR_ADMIN_" + adminId);

        return convertToDTO(soutenanceRepository.save(soutenance));
    }

    // ========== CONSULTATION ==========

    public List<SoutenanceDTO> getAllSoutenances() {
        return soutenanceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<SoutenanceDTO> getSoutenanceById(Long id) {
        return soutenanceRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<SoutenanceDTO> getSoutenancesByDoctorant(Long doctorantId) {
        return soutenanceRepository.findByDoctorantId(doctorantId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SoutenanceDTO> getSoutenancesByStatut(String statut) {
        return soutenanceRepository.findByStatut(statut).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ========== SUPPRESSION ==========

    public void deleteSoutenance(Long id) {
        soutenanceRepository.deleteById(id);
    }

    public void deleteJury(Long id) {
        juryRepository.deleteById(id);
    }

    // ========== VÉRIFICATIONS ==========

    @CircuitBreaker(name = "user-service", fallbackMethod = "checkDoctorantFallback")
    @Retry(name = "user-service")
    public void checkDoctorant(Long doctorantId) {
        UserClient.UserDto user = userClient.getUserById(doctorantId);
        if (user == null || !"DOCTORANT".equals(user.getRole())) {
            throw new IllegalStateException("Utilisateur invalide ou n'est pas un doctorant");
        }
    }

    public void checkDoctorantFallback(Long doctorantId, Throwable t) {
        throw new RuntimeException("Service utilisateur indisponible: " + t.getMessage(), t);
    }

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
    }

    public void checkMembreJuryFallback(Long membreId, Throwable t) {
        throw new RuntimeException(
                "Service utilisateur indisponible pour vérifier le membre " + membreId + ": " + t.getMessage(),
                t
        );
    }

    // ========== CONVERSIONS DTO ==========

    private SoutenanceDTO convertToDTO(Soutenance soutenance) {
        SoutenanceDTO dto = new SoutenanceDTO();
        dto.setId(soutenance.getId());
        dto.setDoctorantId(soutenance.getDoctorantId());
        dto.setDateSoutenance(soutenance.getDateSoutenance());
        dto.setLieu(soutenance.getLieu());
        dto.setStatut(soutenance.getStatut());
        dto.setNombrePublications(soutenance.getNombrePublications());
        dto.setCreditsFormation(soutenance.getCreditsFormation());
        dto.setChecklistValidee(soutenance.getChecklistValidee());

        // Récupérer les infos du doctorant
        try {
            UserClient.UserDto doctorant = userClient.getUserById(soutenance.getDoctorantId());
            if (doctorant != null) {
                dto.setNomDoctorant(doctorant.getNom());
                dto.setPrenomDoctorant(doctorant.getPrenom());
                dto.setEmailDoctorant(doctorant.getEmail());
            }
        } catch (Exception e) {
            // Ignorer si le service est indisponible
        }

        dto.setJury(soutenance.getJury().stream()
                .map(this::convertJuryToDTO)
                .collect(Collectors.toList()));

        dto.setRapports(soutenance.getRapports().stream()
                .map(this::convertRapportToDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private JuryDTO convertJuryToDTO(Jury jury) {
        JuryDTO dto = new JuryDTO();
        dto.setId(jury.getId());
        dto.setMembreId(jury.getMembreId());
        dto.setNomComplet(jury.getNomComplet());
        dto.setEtablissement(jury.getEtablissement());
        dto.setRole(jury.getRole());
        dto.setOrdre(jury.getOrdre());
        return dto;
    }

    private RapportDTO convertRapportToDTO(Rapport rapport) {
        RapportDTO dto = new RapportDTO();
        dto.setId(rapport.getId());
        dto.setAvis(rapport.getAvis());
        dto.setFichierUrl(rapport.getFichierUrl());
        return dto;
    }
}
