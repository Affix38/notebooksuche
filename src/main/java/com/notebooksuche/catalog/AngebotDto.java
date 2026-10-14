package com.notebooksuche.catalog;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Schreibgeschützte Darstellung eines Preisangebots für die API-Antwort.
 * Enthält bewusst nur den Shop-Namen statt der gesamten Shop-Entität.
 *
 * @param shopName         Name des Shops (z. B. "Cyberport")
 * @param preis            Angebotspreis in Euro
 * @param artikelUrl       Link zum Artikel im Shop
 * @param verfuegbarkeit   Verfügbarkeit (z. B. "Sofort lieferbar")
 * @param aktualisiertAm   Zeitpunkt der letzten Aktualisierung des Angebots
 */
public record AngebotDto(
        String shopName,
        BigDecimal preis,
        String artikelUrl,
        String verfuegbarkeit,
        LocalDateTime aktualisiertAm
) {

    /**
     * Erzeugt ein DTO aus einer Angebot-Entität.
     *
     * @param angebot Die Entität (Shop muss geladen sein)
     * @return Das entsprechende DTO
     */
    public static AngebotDto von(Angebot angebot) {
        return new AngebotDto(
                angebot.getShop().getName(),
                angebot.getPreis(),
                angebot.getArtikelUrl(),
                angebot.getVerfuegbarkeit(),
                angebot.getAktualisiertAm()
        );
    }
}
