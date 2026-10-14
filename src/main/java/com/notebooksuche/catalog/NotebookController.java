package com.notebooksuche.catalog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-Controller für die Verwaltung und Suche von Notebooks im Produktkatalog.
 * Delegiert Geschäftsvorgänge an den NotebookService.
 */
@RestController
@RequestMapping("/api/notebooks")
public class NotebookController {

    private final NotebookService notebookService;

    /**
     * Konstruktorbasierte Dependency Injection des Business-Services.
     */
    public NotebookController(NotebookService notebookService) {
        this.notebookService = notebookService;
    }

    /**
     * Gibt eine Liste aller gespeicherten Notebooks zurück.
     * Endpunkt: GET /api/notebooks
     */
    @GetMapping
    public List<Notebook> alleNotebooksAbrufen() {
        return notebookService.alleNotebooksFinden();
    }

    /**
     * Sucht ein bestimmtes Notebook anhand seiner ID.
     * Endpunkt: GET /api/notebooks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Notebook> notebookNachIdAbrufen(@PathVariable Long id) {
        return notebookService.nachIdFinden(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Sucht Notebooks nach Hersteller (Marke).
     * Endpunkt: GET /api/notebooks/suche?marke=Apple
     */
    @GetMapping("/suche")
    public List<Notebook> nachMarkeSuchen(@RequestParam String marke) {
        return notebookService.nachMarkeSuchen(marke);
    }

    /**
     * Validiert und speichert ein neues Notebook in der Datenbank.
     * Endpunkt: POST /api/notebooks
     */
    @PostMapping
    public ResponseEntity<?> notebookErstellen(@RequestBody Notebook notebook) {
        try {
            Notebook gespeichertesNotebook = notebookService.notebookSpeichern(notebook);
            return ResponseEntity.status(HttpStatus.CREATED).body(gespeichertesNotebook);
        } catch (IllegalArgumentException e) {
            // Gibt einen aussagekräftigen 400 Bad Request Fehler zurück, falls Validierungsregeln verletzt wurden
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Erstellt Testdaten in PostgreSQL, falls die Tabelle noch leer ist.
     * Endpunkt: POST /api/notebooks/beispieldaten
     */
    @PostMapping("/beispieldaten")
    public ResponseEntity<String> beispieldatenErstellen() {
        String ergebnis = notebookService.beispieldatenInitialisieren();
        return ResponseEntity.ok(ergebnis);
    }
}
