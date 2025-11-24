package org.example.inscriptionservice.repository;

import org.example.inscriptionservice.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByInscriptionId(Long inscriptionId);
}