package org.example.reservationservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cin;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Double prix;
    @Enumerated(EnumType.STRING)
    private StatutReservation statut;
    private String ticketPdfPath;
    private String cinUploadPath;
    private Long stadeId;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }
    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }
    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }
    public StatutReservation getStatut() { return statut; }
    public void setStatut(StatutReservation statut) { this.statut = statut; }
    public String getTicketPdfPath() { return ticketPdfPath; }
    public void setTicketPdfPath(String ticketPdfPath) { this.ticketPdfPath = ticketPdfPath; }
    public String getCinUploadPath() { return cinUploadPath; }
    public void setCinUploadPath(String cinUploadPath) { this.cinUploadPath = cinUploadPath; }
    public Long getStadeId() { return stadeId; }
    public void setStadeId(Long stadeId) { this.stadeId = stadeId; }
}
