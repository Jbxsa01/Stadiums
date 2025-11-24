package org.example.inscriptionservice.enums;

public enum StatutInscription {
    BROUILLON,           // En cours de saisie
    SOUMISE,             // Doctorant a soumis
    VALIDEE_DIRECTEUR,   // Directeur a validé
    REJETEE_DIRECTEUR,   // Directeur a rejeté
    VALIDEE_ADMIN,       // Administration a validé
    REJETEE_ADMIN,       // Administration a rejeté
    COMPLETEE            // Inscription complète
}
