package org.example.inscriptionservice.service;

import org.example.inscriptionservice.entity.Document;
import org.example.inscriptionservice.enums.TypeDocument;
import org.example.inscriptionservice.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepo;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Document uploadDocument(Long inscriptionId, MultipartFile file, TypeDocument typeDocument) {
        try {
            // Créer le dossier s'il n'existe pas
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Générer un nom unique
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // Sauvegarder le fichier
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);

            // Créer l'entité Document
            Document document = new Document();
            document.setInscriptionId(inscriptionId);
            document.setNomFichier(originalFilename);
            document.setCheminFichier(filePath.toString());
            document.setTypeDocument(typeDocument);
            document.setTailleFichier(file.getSize());
            document.setContentType(file.getContentType());
            document.setDateUpload(LocalDateTime.now());

            return documentRepo.save(document);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du document", e);
        }
    }

    public List<Document> getDocumentsByInscription(Long inscriptionId) {
        return documentRepo.findByInscriptionId(inscriptionId);
    }

    public Document getDocumentById(Long id) {
        return documentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));
    }

    public void deleteDocument(Long id) {
        Document document = getDocumentById(id);

        // Supprimer le fichier physique
        try {
            Path filePath = Paths.get(document.getCheminFichier());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la suppression du fichier", e);
        }

        // Supprimer l'entité
        documentRepo.deleteById(id);
    }
}