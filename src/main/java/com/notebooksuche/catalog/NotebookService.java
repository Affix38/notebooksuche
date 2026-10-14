package com.notebooksuche.catalog;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service-Klasse für die Geschäftslogik rund um Notebooks im Katalog.
 */
@Service
@Transactional(readOnly = true)
public class NotebookService {

    private final NotebookRepository notebookRepository;

    public NotebookService(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }

    /**
     * Gibt alle vorhandenen Notebooks zurück.
     */
    public List<Notebook> alleNotebooksFinden() {
        return notebookRepository.findAll();
    }

    /**
     * Sucht ein Notebook anhand der eindeutigen Datenbank-ID.
     */
    public Optional<Notebook> nachIdFinden(Long id) {
        return notebookRepository.findById(id);
    }

    /**
     * Sucht ein Notebook anhand der EAN.
     */
    public Optional<Notebook> nachEanFinden(String ean) {
        return notebookRepository.findByEan(ean);
    }

    /**
     * Sucht alle Notebooks einer bestimmten Marke (Groß-/Kleinschreibung ignoriert).
     */
    public List<Notebook> nachMarkeSuchen(String marke) {
        if (marke == null || marke.isBlank()) {
            return List.of();
        }
        return notebookRepository.findByMarkeIgnoreCase(marke.trim());
    }

    /**
     * Validiert und speichert ein neues Notebook.
     * Führt Geschäftsregeln durch: Pflichtfelder, positive UVP (falls angegeben) und EAN-Eindeutigkeit.
     */
    @Transactional
    public Notebook notebookSpeichern(Notebook notebook) {
        if (notebook.getMarke() == null || notebook.getMarke().isBlank()) {
            throw new IllegalArgumentException("Die Marke darf nicht leer sein.");
        }

        if (notebook.getModell() == null || notebook.getModell().isBlank()) {
            throw new IllegalArgumentException("Das Modell darf nicht leer sein.");
        }

        // Die UVP ist optional; wenn angegeben, muss sie positiv sein.
        if (notebook.getUvpPreis() != null && notebook.getUvpPreis().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Die UVP muss größer als 0 sein.");
        }

        // Prüfung auf Duplikate der EAN vor dem Speichern
        if (notebook.getEan() != null && !notebook.getEan().isBlank()) {
            Optional<Notebook> existierendesNotebook = notebookRepository.findByEan(notebook.getEan());
            if (existierendesNotebook.isPresent() && !existierendesNotebook.get().getId().equals(notebook.getId())) {
                throw new IllegalArgumentException("Ein Notebook mit der EAN " + notebook.getEan() + " existiert bereits im Katalog.");
            }
        }

        return notebookRepository.save(notebook);
    }

    /**
     * Erstellt Testdaten im Katalog, falls die Datenbank noch leer ist.
     */
    @Transactional
    public String beispieldatenInitialisieren() {
        if (notebookRepository.count() > 0) {
            return "Beispieldaten sind bereits in der Datenbank vorhanden.";
        }

        Notebook macbook = new Notebook(
                "Apple",
                "MacBook Air M2",
                "0194253081923",
                new BigDecimal("1099.00"),
                "13.6 Zoll Liquid Retina Display, 8 GB RAM, 256 GB SSD Speicher, Mitternacht"
        );

        Notebook thinkpad = new Notebook(
                "Lenovo",
                "ThinkPad T14 Gen 4",
                "0195892019482",
                new BigDecimal("1249.50"),
                "14 Zoll WUXGA Display, Intel Core i5-1335U, 16 GB RAM, 512 GB SSD"
        );

        Notebook zenbook = new Notebook(
                "Asus",
                "Zenbook 14 OLED",
                "0197283910283",
                new BigDecimal("899.99"),
                "14 Zoll 2.8K OLED Display 90Hz, Intel Core Ultra 5, 16 GB RAM, 512 GB SSD"
        );

        notebookRepository.saveAll(List.of(macbook, thinkpad, zenbook));
        return "3 Beispiel-Notebooks wurden erfolgreich in der PostgreSQL-Datenbank gespeichert!";
    }
}
