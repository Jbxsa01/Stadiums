package org.example.inscriptionservice.service;

import org.example.inscriptionservice.client.ResilientClient;
import org.example.inscriptionservice.client.UserClient;
import org.example.inscriptionservice.dto.UserResponse;
import org.example.inscriptionservice.dto.UserResponseWrapper;
import org.example.inscriptionservice.entity.Inscription;
import org.example.inscriptionservice.enums.Role;
import org.example.inscriptionservice.enums.StatutInscription;
import org.example.inscriptionservice.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InscriptionService {

    private final InscriptionRepository inscriptionRepo;
    private final ResilientClient userClient;  // ← Injection du client Feign

    // Créer un brouillon d'inscription avec validation
    public Inscription createInscription(Inscription inscription) {
        log.info("Création d'une nouvelle inscription pour doctorant: {}", inscription.getDoctorantId());

        try {
            // Vérifier le doctorant via Feign
            UserResponseWrapper doctorantWrapper = userClient.getUserById(inscription.getDoctorantId());
            UserResponse doctorant = doctorantWrapper.getData();
            validateUser(doctorant, Role.DOCTORANT);
            log.info("Doctorant validé: {} {}", doctorant.getPrenom(), doctorant.getNom());

        } catch (Exception e) {
            log.error("Erreur lors de la récupération du doctorant: {}", e.getMessage());
            throw new RuntimeException("Doctorant non trouvé ou invalide: " + inscription.getDoctorantId());
        }

        try {
            // Vérifier le directeur via Feign
            UserResponseWrapper directeurWrapper = userClient.getUserById(inscription.getDirecteurId());
            UserResponse directeur = directeurWrapper.getData();
            validateUser(directeur, Role.DIRECTEUR);
            log.info("Directeur validé: {} {}", directeur.getPrenom(), directeur.getNom());

        } catch (Exception e) {
            log.error("Erreur lors de la récupération du directeur: {}", e.getMessage());
            throw new RuntimeException("Directeur non trouvé ou invalide: " + inscription.getDirecteurId());
        }

        inscription.setStatut(StatutInscription.BROUILLON);
        inscription.setDateCreation(LocalDateTime.now());
        return inscriptionRepo.save(inscription);
    }

    // Méthode de validation
    private void validateUser(UserResponse user, Role expectedRole) {
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        if (user.getRole() != expectedRole) {
            throw new RuntimeException("Rôle incorrect. Attendu: " + expectedRole + ", Reçu: " + user.getRole());
        }
    }

    // ... reste des méthodes inchangées ...

    public Inscription soumettreInscription(Long id) {
        Inscription inscription = getInscriptionById(id);

        if (inscription.getStatut() != StatutInscription.BROUILLON) {
            throw new RuntimeException("Seuls les brouillons peuvent être soumis");
        }

        inscription.setStatut(StatutInscription.SOUMISE);
        inscription.setDateModification(LocalDateTime.now());
        return inscriptionRepo.save(inscription);
    }

    public Inscription validerParDirecteur(Long id, Boolean valider, String commentaire) {
        Inscription inscription = getInscriptionById(id);

        if (inscription.getStatut() != StatutInscription.SOUMISE) {
            throw new RuntimeException("L'inscription doit être soumise");
        }

        inscription.setStatut(valider ?
                StatutInscription.VALIDEE_DIRECTEUR :
                StatutInscription.REJETEE_DIRECTEUR);
        inscription.setCommentaireDirecteur(commentaire);
        inscription.setDateValidationDirecteur(LocalDateTime.now());
        inscription.setDateModification(LocalDateTime.now());

        return inscriptionRepo.save(inscription);
    }

    public Inscription validerParAdmin(Long id, Boolean valider, String commentaire) {
        Inscription inscription = getInscriptionById(id);

        if (inscription.getStatut() != StatutInscription.VALIDEE_DIRECTEUR) {
            throw new RuntimeException("L'inscription doit être validée par le directeur");
        }

        inscription.setStatut(valider ?
                StatutInscription.VALIDEE_ADMIN :
                StatutInscription.REJETEE_ADMIN);
        inscription.setCommentaireAdmin(commentaire);
        inscription.setDateValidationAdmin(LocalDateTime.now());
        inscription.setDateModification(LocalDateTime.now());

        return inscriptionRepo.save(inscription);
    }

    public List<Inscription> getAllInscriptions() {
        return inscriptionRepo.findAll();
    }

    public Inscription getInscriptionById(Long id) {
        return inscriptionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée"));
    }

    public List<Inscription> getInscriptionsByDoctorant(Long doctorantId) {
        return inscriptionRepo.findByDoctorantId(doctorantId);
    }

    public List<Inscription> getInscriptionsByDirecteur(Long directeurId) {
        return inscriptionRepo.findByDirecteurId(directeurId);
    }

    public List<Inscription> getInscriptionsByCampagne(Long campagneId) {
        return inscriptionRepo.findByCampagneId(campagneId);
    }

    public List<Inscription> getInscriptionsEnAttenteDirecteur() {
        return inscriptionRepo.findByStatut(StatutInscription.SOUMISE);
    }

    public List<Inscription> getInscriptionsEnAttenteAdmin() {
        return inscriptionRepo.findByStatut(StatutInscription.VALIDEE_DIRECTEUR);
    }

    public Inscription updateInscription(Long id, Inscription inscription) {
        Inscription existing = getInscriptionById(id);

        if (existing.getStatut() != StatutInscription.BROUILLON) {
            throw new RuntimeException("Seuls les brouillons peuvent être modifiés");
        }

        existing.setSujetThese(inscription.getSujetThese());
        existing.setDescriptionProjet(inscription.getDescriptionProjet());
        existing.setDomaineRecherche(inscription.getDomaineRecherche());
        existing.setDateModification(LocalDateTime.now());

        return inscriptionRepo.save(existing);
    }

    public void deleteInscription(Long id) {
        inscriptionRepo.deleteById(id);
    }
}
