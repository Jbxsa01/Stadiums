package org.example.inscriptionservice.model;

import jakarta.persistence.*;

@Entity
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // JOURNAL or CONFERENCE
    private String rang; // Q1, Q2, etc. pour les journaux
    private String titre;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRang() {
        return rang;
    }

    public void setRang(String rang) {
        this.rang = rang;
    }

    // ...autres getters/setters
}
