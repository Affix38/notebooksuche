package com.notebooksuche.catalog;

import java.math.BigDecimal;
import java.util.List;

/**
 * Ergebnis des Preisvergleichs für ein Notebook: Stammdaten, günstigster Preis
 * und alle Angebote (aufsteigend nach Preis sortiert).
 *
 * @param notebookId        ID des Notebooks
 * @param marke             Marke (z. B. "Lenovo")
 * @param modell            Modellbezeichnung
 * @param ean               EAN des Notebooks
 * @param beschreibung      Beschreibung des Notebooks
 * @param uvpPreis          Unverbindliche Preisempfehlung des Herstellers; null, wenn unbekannt
 * @param guenstigsterPreis Niedrigster Preis aller Angebote; null, wenn es keine Angebote gibt
 * @param anzahlAngebote    Anzahl der vorhandenen Angebote
 * @param angebote          Alle Angebote, günstigstes zuerst
 */
public record PreisvergleichDto(
        Long notebookId,
        String marke,
        String modell,
        String ean,
        String beschreibung,
        BigDecimal uvpPreis,
        BigDecimal guenstigsterPreis,
        int anzahlAngebote,
        List<AngebotDto> angebote
) {
}
