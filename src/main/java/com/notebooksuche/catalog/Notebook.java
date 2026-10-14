package com.notebooksuche.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entität zur Speicherung von Notebook-Informationen im Produktkatalog.
 */
@Entity
@Table(name = "notebooks")
public class Notebook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String marke;

    @Column(nullable = false)
    private String modell;

    @Column(unique = true)
    private String ean; // European Article Number für den Preisvergleich

    /**
     * Unverbindliche Preisempfehlung des Herstellers (UVP). Optional: nicht für jedes Notebook bekannt.
     * Die tatsächlichen Verkaufspreise stehen in den Angeboten der Shops.
     */
    @Column(name = "uvp_preis", precision = 10, scale = 2)
    private BigDecimal uvpPreis;

    @Column(columnDefinition = "TEXT")
    private String beschreibung;

    @Column(name = "erstellt_am", nullable = false, updatable = false)
    private LocalDateTime erstelltAm;

    public Notebook() {
    }

    public Notebook(String marke, String modell, String ean, BigDecimal uvpPreis, String beschreibung) {
        this.marke = marke;
        this.modell = modell;
        this.ean = ean;
        this.uvpPreis = uvpPreis;
        this.beschreibung = beschreibung;
    }

    @PrePersist
    protected void vorDemSpeichern() {
        this.erstelltAm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarke() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke = marke;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public BigDecimal getUvpPreis() {
        return uvpPreis;
    }

    public void setUvpPreis(BigDecimal uvpPreis) {
        this.uvpPreis = uvpPreis;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public LocalDateTime getErstelltAm() {
        return erstelltAm;
    }

    public void setErstelltAm(LocalDateTime erstelltAm) {
        this.erstelltAm = erstelltAm;
    }
}
