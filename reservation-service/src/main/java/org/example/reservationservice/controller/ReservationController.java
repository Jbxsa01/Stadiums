package org.example.reservationservice.controller;

import org.example.reservationservice.model.Reservation;
import org.example.reservationservice.model.StatutReservation;
import org.example.reservationservice.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Reservation> create(@RequestPart("reservation") String reservationJson,
                                             @RequestPart("cinFile") MultipartFile cinFile) throws IOException {
        Reservation reservation = objectMapper.readValue(reservationJson, Reservation.class);
        return ResponseEntity.ok(reservationService.createReservation(reservation, cinFile));
    }

    @GetMapping
    public List<Reservation> getAll() {
        return reservationService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable Long id) {
        return reservationService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> update(@PathVariable Long id, @RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.updateReservation(id, reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<Reservation> changeStatut(@PathVariable Long id, @RequestParam StatutReservation statut) {
        return ResponseEntity.ok(reservationService.changeStatut(id, statut));
    }
}
