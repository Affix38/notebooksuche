package com.notebooksuche.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository-Schnittstelle für den Datenbankzugriff auf Notebook-Entitäten.
 * Nutzt Spring Data JPA zur automatischen Abfrageerstellung.
 */
@Repository
public interface NotebookRepository extends JpaRepository<Notebook, Long> {

    /**
     * Sucht ein Notebook anhand der eindeutigen EAN-Nummer (für den Preisvergleich).
     *
     * @param ean Die europäische Artikelnummer (EAN)
     * @return Ein Optional mit dem gefundenen Notebook
     */
    Optional<Notebook> findByEan(String ean);

    /**
     * Sucht alle Notebooks eines bestimmten Herstellers (Groß-/Kleinschreibung wird ignoriert).
     *
     * @param marke Der Markenname (z. B. Apple, Lenovo)
     * @return Liste der passenden Notebooks
     */
    List<Notebook> findByMarkeIgnoreCase(String marke);
}
