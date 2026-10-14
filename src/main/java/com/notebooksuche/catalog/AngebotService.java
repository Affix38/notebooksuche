package com.notebooksuche.catalog;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service-Klasse für die Geschäftslogik rund um Preisangebote und den Preisvergleich.
 */
@Service
@Transactional(readOnly = true)
public class AngebotService {

    private final AngebotRepository angebotRepository;
    private final NotebookRepository notebookRepository;
    private final ShopRepository shopRepository;

    public AngebotService(AngebotRepository angebotRepository,
                          NotebookRepository notebookRepository,
                          ShopRepository shopRepository) {
        this.angebotRepository = angebotRepository;
        this.notebookRepository = notebookRepository;
        this.shopRepository = shopRepository;
    }

    /**
     * Gibt alle Angebote eines Notebooks zurück, aufsteigend nach Preis sortiert.
     *
     * @throws NichtGefundenException wenn das Notebook nicht existiert
     */
    public List<AngebotDto> angeboteFuerNotebook(Long notebookId) {
        notebookFinden(notebookId);
        return angebotRepository.findByNotebookIdOrderByPreisAsc(notebookId).stream()
                .map(AngebotDto::von)
                .toList();
    }
    /**
     * Erstellt den Preisvergleich für ein Notebook: Stammdaten, günstigster Preis und alle Angebote.
     *
     * @throws NichtGefundenException wenn das Notebook nicht existiert
     */
    public PreisvergleichDto preisvergleichErstellen(Long notebookId) {
        Notebook notebook = notebookFinden(notebookId);

        List<AngebotDto> angebote = angebotRepository.findByNotebookIdOrderByPreisAsc(notebookId).stream()
                .map(AngebotDto::von)
                .toList();

        // Die Liste ist aufsteigend sortiert: Das erste Angebot ist das günstigste.
        BigDecimal guenstigsterPreis = angebote.isEmpty() ? null : angebote.get(0).preis();

        return new PreisvergleichDto(
                notebook.getId(),
                notebook.getMarke(),
                notebook.getModell(),
                notebook.getEan(),
                notebook.getBeschreibung(),
                notebook.getUvpPreis(),
                guenstigsterPreis,
                angebote.size(),
                angebote
        );
    }

    /**
     * Validiert und speichert ein neues Angebot.
     * Geschäftsregeln: positiver Preis, nicht leere URL und Verfügbarkeit,
     * Notebook und Shop müssen existieren, der Shop muss aktiv sein
     * und darf für dieses Notebook noch kein Angebot haben.
     *
     * @throws NichtGefundenException   wenn Notebook oder Shop nicht existieren
     * @throws IllegalArgumentException bei ungültigen Eingabedaten
     */
    @Transactional
    public AngebotDto angebotErstellen(AngebotAnlegenDto eingabe) {
        if (eingabe.notebookId() == null || eingabe.shopId() == null) {
            throw new IllegalArgumentException("Notebook-ID und Shop-ID müssen angegeben werden.");
        }

        if (eingabe.preis() == null || eingabe.preis().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Der Preis muss größer als 0 sein.");
        }

        if (eingabe.artikelUrl() == null || eingabe.artikelUrl().isBlank()) {
            throw new IllegalArgumentException("Die Artikel-URL darf nicht leer sein.");
        }

        if (eingabe.verfuegbarkeit() == null || eingabe.verfuegbarkeit().isBlank()) {
            throw new IllegalArgumentException("Die Verfügbarkeit darf nicht leer sein.");
        }

        Notebook notebook = notebookFinden(eingabe.notebookId());
        Shop shop = shopRepository.findById(eingabe.shopId())
                .orElseThrow(() -> new NichtGefundenException(
                        "Shop mit der ID " + eingabe.shopId() + " wurde nicht gefunden."));

        if (!shop.isAktiv()) {
            throw new IllegalArgumentException("Der Shop " + shop.getName() + " ist nicht aktiv.");
        }

        // Pro Notebook darf jeder Shop nur ein Angebot haben, sonst wäre der Preisvergleich widersprüchlich.
        if (angebotRepository.existsByNotebookIdAndShopId(notebook.getId(), shop.getId())) {
            throw new IllegalArgumentException("Der Shop " + shop.getName()
                    + " hat bereits ein Angebot für dieses Notebook.");
        }

        Angebot angebot = new Angebot(
                eingabe.preis(),
                eingabe.artikelUrl().trim(),
                eingabe.verfuegbarkeit().trim(),
                notebook,
                shop
        );

        return AngebotDto.von(angebotRepository.save(angebot));
    }

    /**
     * Erstellt Testdaten für den Preisvergleich: drei Shops und je drei Angebote
     * für die drei Beispiel-Notebooks (siehe NotebookService.beispieldatenInitialisieren()).
     * Läuft nur, wenn noch keine Shops und Angebote existieren und die Notebooks vorhanden sind.
     */
    @Transactional
    public String beispieldatenInitialisieren() {
        if (shopRepository.count() > 0 || angebotRepository.count() > 0) {
            return "Beispieldaten für Shops und Angebote sind bereits in der Datenbank vorhanden.";
        }

        Notebook macbook = notebookRepository.findByEan("0194253081923").orElse(null);
        Notebook thinkpad = notebookRepository.findByEan("0195892019482").orElse(null);
        Notebook zenbook = notebookRepository.findByEan("0197283910283").orElse(null);

        if (macbook == null || thinkpad == null || zenbook == null) {
            return "Die Beispiel-Notebooks fehlen. Bitte zuerst POST /api/notebooks/beispieldaten ausführen.";
        }

        Shop cyberport = new Shop("Cyberport", "https://www.cyberport.de", null);
        Shop mediaMarkt = new Shop("MediaMarkt", "https://www.mediamarkt.de", null);
        Shop notebooksbilliger = new Shop("Notebooksbilliger", "https://www.notebooksbilliger.de", null);
        shopRepository.saveAll(List.of(cyberport, mediaMarkt, notebooksbilliger));

        List<Angebot> angebote = List.of(
                new Angebot(new BigDecimal("1049.00"), "https://www.cyberport.de/macbook-air-m2",
                        "Sofort lieferbar", macbook, cyberport),
                new Angebot(new BigDecimal("1099.00"), "https://www.mediamarkt.de/macbook-air-m2",
                        "Lieferzeit 2-3 Werktage", macbook, mediaMarkt),
                new Angebot(new BigDecimal("1029.90"), "https://www.notebooksbilliger.de/macbook-air-m2",
                        "Sofort lieferbar", macbook, notebooksbilliger),

                new Angebot(new BigDecimal("1299.00"), "https://www.cyberport.de/thinkpad-t14-gen4",
                        "Lieferzeit 2-4 Werktage", thinkpad, cyberport),
                new Angebot(new BigDecimal("1249.50"), "https://www.mediamarkt.de/thinkpad-t14-gen4",
                        "Sofort lieferbar", thinkpad, mediaMarkt),
                new Angebot(new BigDecimal("1219.00"), "https://www.notebooksbilliger.de/thinkpad-t14-gen4",
                        "Lieferzeit 5-7 Werktage", thinkpad, notebooksbilliger),

                new Angebot(new BigDecimal("899.99"), "https://www.cyberport.de/zenbook-14-oled",
                        "Sofort lieferbar", zenbook, cyberport),
                new Angebot(new BigDecimal("949.00"), "https://www.mediamarkt.de/zenbook-14-oled",
                        "Sofort lieferbar", zenbook, mediaMarkt),
                new Angebot(new BigDecimal("879.00"), "https://www.notebooksbilliger.de/zenbook-14-oled",
                        "Lieferzeit 2-3 Werktage", zenbook, notebooksbilliger)
        );
        angebotRepository.saveAll(angebote);

        return "3 Shops und 9 Angebote wurden erfolgreich gespeichert!";
    }

    private Notebook notebookFinden(Long notebookId) {
        return notebookRepository.findById(notebookId)
                .orElseThrow(() -> new NichtGefundenException(
                        "Notebook mit der ID " + notebookId + " wurde nicht gefunden."));
    }
}
