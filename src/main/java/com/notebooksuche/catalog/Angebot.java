package com.notebooksuche.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entität für ein konkretes Preisangebot eines bestimmten Shops für ein Notebook.
 * Das Kernstück des Preisvergleichs-Aggregators.
 */
@Entity
@Table(name = "angebote")
public class Angebot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preis;

    @Column(name = "artikel_url", nullable = false, columnDefinition = "TEXT")
    private String artikelUrl;

    @Column(nullable = false)
    private String verfuegbarkeit; // z. B. "Sofort lieferbar", "Lieferzeit 2-4 Werktage"

    @Column(name = "aktualisiert_am", nullable = false)
    private LocalDateTime aktualisiertAm;

    /**
     * Viele Angebote gehören zu einem Notebook (N:1-Beziehung).
     * Erzeugt die Fremdschlüssel-Spalte "notebook_id" in der Tabelle "angebote".
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "notebook_id", nullable = false)
    private Notebook notebook;

    /**
     * Viele Angebote gehören zu einem Partnershop (N:1-Beziehung).
     * Erzeugt die Fremdschlüssel-Spalte "shop_id" in der Tabelle "angebote".
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    public Angebot() {
    }

    public Angebot(BigDecimal preis, String artikelUrl, String verfuegbarkeit, Notebook notebook, Shop shop) {
        this.preis = preis;
        this.artikelUrl = artikelUrl;
        this.verfuegbarkeit = verfuegbarkeit;
        this.notebook = notebook;
        this.shop = shop;
    }

    @PrePersist
    @PreUpdate
    protected void vorDemSpeichernOderAktualisieren() {
        this.aktualisiertAm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPreis() {
        return preis;
    }

    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    public String getArtikelUrl() {
        return artikelUrl;
    }

    public void setArtikelUrl(String artikelUrl) {
        this.artikelUrl = artikelUrl;
    }

    public String getVerfuegbarkeit() {
        return verfuegbarkeit;
    }

    public void setVerfuegbarkeit(String verfuegbarkeit) {
        this.verfuegbarkeit = verfuegbarkeit;
    }

    public LocalDateTime getAktualisiertAm() {
        return aktualisiertAm;
    }

    public void setAktualisiertAm(LocalDateTime aktualisiertAm) {
        this.aktualisiertAm = aktualisiertAm;
    }

    public Notebook getNotebook() {
        return notebook;
    }

    public void setNotebook(Notebook notebook) {
        this.notebook = notebook;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
