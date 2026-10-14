package com.notebooksuche.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository-Schnittstelle für den Datenbankzugriff auf Partnershops.
 */
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    /**
     * Findet einen Shop anhand seines Namens (Groß-/Kleinschreibung ignoriert).
     *
     * @param name Der Name des Shops (z. B. "Cyberport")
     * @return Ein Optional mit dem Shop
     */
    Optional<Shop> findByNameIgnoreCase(String name);

    /**
     * Prüft, ob ein Shop mit diesem Namen bereits existiert.
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Gibt alle aktiven Partnershops zurück.
     */
    List<Shop> findByAktivTrue();
}
