package com.notebooksuche.catalog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Zentrale Fehlerbehandlung für die REST-Controller.
 * Wandelt Fachausnahmen der Service-Schicht in passende HTTP-Antworten um.
 */
@RestControllerAdvice
public class ApiFehlerBehandlung {

    /**
     * Ressource nicht vorhanden: HTTP 404 Not Found.
     */
    @ExceptionHandler(NichtGefundenException.class)
    public ResponseEntity<String> nichtGefunden(NichtGefundenException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Verletzte Validierungsregel: HTTP 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> ungueltigeEingabe(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
