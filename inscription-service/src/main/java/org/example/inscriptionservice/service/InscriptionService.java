package org.example.inscriptionservice.service;

import org.example.inscriptionservice.entity.Inscription;
import org.example.inscriptionservice.enums.StatutInscription;
import org.example.inscriptionservice.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InscriptionService {

    private final InscriptionRepository inscriptionRepo;

    // Créer un brouillon d'inscription
    public Inscription createInscription(Inscription inscription) {
        inscription.setStatut(StatutInscription.BROUILLON);
        inscription.setDateCreation(LocalDateTime.now());
        return inscriptionRepo.save(inscription);
    }
    // Soumettre l'inscription (doctorant)
    public Inscription soumettreInscription(Long id) {
        Inscription inscription = getInscriptionById(id);

        if (inscription.getStatut() != StatutInscription.BROUILLON) {
            throw new RuntimeException("Seuls les brouillons peuvent être soumis");
        }

        inscription.setStatut(StatutInscription.SOUMISE);
        inscription.setDateModification(LocalDateTime.now());
        return inscriptionRepo.save(inscription);
    }

    // Validation par le directeur
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

    // Validation par l'administration
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

    // Obtenir toutes les inscriptions
    public List<Inscription> getAllInscriptions() {
        return inscriptionRepo.findAll();
    }

    // Obtenir une inscription par ID
    public Inscription getInscriptionById(Long id) {
        return inscriptionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée"));
    }

    // Obtenir les inscriptions d'un doctorant
    public List<Inscription> getInscriptionsByDoctorant(Long doctorantId) {
        return inscriptionRepo.findByDoctorantId(doctorantId);
    }

    // Obtenir les inscriptions d'un directeur
    public List<Inscription> getInscriptionsByDirecteur(Long directeurId) {
        return inscriptionRepo.findByDirecteurId(directeurId);
    }

    // Obtenir les inscriptions d'une campagne
    public List<Inscription> getInscriptionsByCampagne(Long campagneId) {
        return inscriptionRepo.findByCampagneId(campagneId);
    }

    // Obtenir les inscriptions en attente de validation directeur
    public List<Inscription> getInscriptionsEnAttenteDirecteur() {
        return inscriptionRepo.findByStatut(StatutInscription.SOUMISE);
    }

    // Obtenir les inscriptions en attente de validation admin
    public List<Inscription> getInscriptionsEnAttenteAdmin() {
        return inscriptionRepo.findByStatut(StatutInscription.VALIDEE_DIRECTEUR);
    }

    // Mettre à jour une inscription
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

    // Supprimer une inscription
    public void deleteInscription(Long id) {
        inscriptionRepo.deleteById(id);
    }
}
