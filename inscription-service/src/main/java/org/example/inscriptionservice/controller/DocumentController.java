package org.example.inscriptionservice.controller;

import org.example.inscriptionservice.entity.Document;
import org.example.inscriptionservice.enums.TypeDocument;
import org.example.inscriptionservice.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/inscription/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(
            @RequestParam Long inscriptionId,
            @RequestParam TypeDocument typeDocument,
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentService.uploadDocument(inscriptionId, file, typeDocument));
    }

    @GetMapping("/inscription/{inscriptionId}")
    public ResponseEntity<List<Document>> getDocumentsByInscription(@PathVariable Long inscriptionId) {
        return ResponseEntity.ok(documentService.getDocumentsByInscription(inscriptionId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}