package com.notebooksuche.catalog;

/**
 * Wird geworfen, wenn eine angeforderte Ressource (Notebook, Shop usw.) nicht existiert.
 * Soll später auf den HTTP-Status 404 abgebildet werden.
 */
public class NichtGefundenException extends RuntimeException {

    public NichtGefundenException(String nachricht) {
        super(nachricht);
    }
}
