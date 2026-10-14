package com.notebooksuche.catalog;

import java.math.BigDecimal;

/**
 * Eingabedaten zum Anlegen eines neuen Preisangebots (Request-Body von POST /api/angebote).
 *
 * @param notebookId     ID des Notebooks, zu dem das Angebot gehört
 * @param shopId         ID des Shops, der das Angebot bereitstellt
 * @param preis          Angebotspreis in Euro (muss größer als 0 sein)
 * @param artikelUrl     Link zum Artikel im Shop (darf nicht leer sein)
 * @param verfuegbarkeit Verfügbarkeit (z. B. "Sofort lieferbar")
 */
public record AngebotAnlegenDto(
        Long notebookId,
        Long shopId,
        BigDecimal preis,
        String artikelUrl,
        String verfuegbarkeit
) {
}
