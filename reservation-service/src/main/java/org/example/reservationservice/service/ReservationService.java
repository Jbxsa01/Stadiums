package org.example.reservationservice.service;

import org.example.reservationservice.model.Reservation;
import org.example.reservationservice.model.StatutReservation;
import org.example.reservationservice.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    // Simule le prix du stade (à remplacer par appel microservice stade)
    private double getPrixStade(Long stadeId) {
        return 100.0; // exemple
    }

    public Reservation createReservation(Reservation reservation, MultipartFile cinFile) throws IOException {
        // Upload CIN
        String cinPath = saveCinFile(cinFile);
        reservation.setCinUploadPath(cinPath);
        // Calcul prix
        double prixStade = getPrixStade(reservation.getStadeId());
        long heures = Duration.between(reservation.getHeureDebut(), reservation.getHeureFin()).toHours();
        reservation.setPrix(prixStade * heures);
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation updateReservation(Long id, Reservation updated) {
        return reservationRepository.findById(id).map(r -> {
            r.setDate(updated.getDate());
            r.setHeureDebut(updated.getHeureDebut());
            r.setHeureFin(updated.getHeureFin());
            r.setStadeId(updated.getStadeId());
            // recalcul prix
            double prixStade = getPrixStade(updated.getStadeId());
            long heures = Duration.between(updated.getHeureDebut(), updated.getHeureFin()).toHours();
            r.setPrix(prixStade * heures);
            return reservationRepository.save(r);
        }).orElseThrow();
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public Reservation changeStatut(Long id, StatutReservation statut) {
        return reservationRepository.findById(id).map(r -> {
            r.setStatut(statut);
            // Générer ticket PDF si confirmé (à implémenter)
            return reservationRepository.save(r);
        }).orElseThrow();
    }

    private String saveCinFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;
        String folder = "uploads/cin/";
        Files.createDirectories(Paths.get(folder));
        String filePath = folder + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(filePath);
        file.transferTo(path);
        return filePath;
    }
}
