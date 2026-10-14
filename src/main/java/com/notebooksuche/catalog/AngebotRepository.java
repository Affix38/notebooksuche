package com.notebooksuche.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository-Schnittstelle für den Datenbankzugriff auf Preisangebote.
 */
@Repository
public interface AngebotRepository extends JpaRepository<Angebot, Long> {

    /**
     * Gibt alle Angebote für ein Notebook zurück, aufsteigend nach Preis sortiert
     * (das günstigste Angebot steht zuerst).
     *
     * @param notebookId Die ID des Notebooks
     * @return Eine nach Preis sortierte Liste der Angebote
     */
    List<Angebot> findByNotebookIdOrderByPreisAsc(Long notebookId);

    /**
     * Prüft, ob ein Shop bereits ein Angebot für das Notebook hat.
     *
     * @param notebookId Die ID des Notebooks
     * @param shopId     Die ID des Shops
     */
    boolean existsByNotebookIdAndShopId(Long notebookId, Long shopId);

    /**
     * Gibt das günstigste Angebot für ein Notebook zurück.
     *
     * @param notebookId Die ID des Notebooks
     * @return Ein Optional mit dem günstigsten Angebot (leer, wenn keine Angebote existieren)
     */
    Optional<Angebot> findFirstByNotebookIdOrderByPreisAsc(Long notebookId);
}
