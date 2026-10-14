package com.notebooksuche.catalog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-Controller für Preisangebote und den Preisvergleich.
 * Delegiert Geschäftsvorgänge an den AngebotService.
 */
@RestController
@RequestMapping("/api")
public class AngebotController {

    private final AngebotService angebotService;

    public AngebotController(AngebotService angebotService) {
        this.angebotService = angebotService;
    }

    /**
     * Gibt alle Angebote eines Notebooks zurück, aufsteigend nach Preis sortiert.
     * Endpunkt: GET /api/notebooks/{id}/angebote
     */
    @GetMapping("/notebooks/{id}/angebote")
    public List<AngebotDto> angeboteAbrufen(@PathVariable Long id) {
        return angebotService.angeboteFuerNotebook(id);
    }

    /**
     * Gibt den Preisvergleich eines Notebooks zurück (günstigster Preis und alle Angebote).
     * Endpunkt: GET /api/notebooks/{id}/preisvergleich
     */
    @GetMapping("/notebooks/{id}/preisvergleich")
    public PreisvergleichDto preisvergleichAbrufen(@PathVariable Long id) {
        return angebotService.preisvergleichErstellen(id);
    }

    /**
     * Legt ein neues Angebot an (404 bei unbekanntem Notebook/Shop, 400 bei Validierungsfehlern).
     * Endpunkt: POST /api/angebote
     */
    @PostMapping("/angebote")
    public ResponseEntity<AngebotDto> angebotErstellen(@RequestBody AngebotAnlegenDto eingabe) {
        AngebotDto angebot = angebotService.angebotErstellen(eingabe);
        return ResponseEntity.status(HttpStatus.CREATED).body(angebot);
    }

    /**
     * Erstellt Testdaten (Shops und Angebote), falls noch keine vorhanden sind.
     * Endpunkt: POST /api/angebote/beispieldaten
     */
    @PostMapping("/angebote/beispieldaten")
    public ResponseEntity<String> beispieldatenErstellen() {
        return ResponseEntity.ok(angebotService.beispieldatenInitialisieren());
    }
}
