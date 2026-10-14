package com.notebooksuche.catalog;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service-Klasse für die Geschäftslogik rund um Partnershops.
 */
@Service
@Transactional(readOnly = true)
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    /**
     * Gibt alle vorhandenen Shops zurück.
     */
    public List<Shop> alleShopsFinden() {
        return shopRepository.findAll();
    }

    /**
     * Sucht einen Shop anhand der Datenbank-ID.
     *
     * @throws NichtGefundenException wenn kein Shop mit dieser ID existiert
     */
    public Shop nachIdFinden(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new NichtGefundenException("Shop mit der ID " + id + " wurde nicht gefunden."));
    }

    /**
     * Validiert und speichert einen neuen Shop.
     * Geschäftsregeln: Name und Website-URL sind Pflicht, der Name muss eindeutig sein.
     */
    @Transactional
    public Shop shopSpeichern(Shop shop) {
        if (shop.getName() == null || shop.getName().isBlank()) {
            throw new IllegalArgumentException("Der Name des Shops darf nicht leer sein.");
        }

        if (shop.getWebsiteUrl() == null || shop.getWebsiteUrl().isBlank()) {
            throw new IllegalArgumentException("Die Website-URL des Shops darf nicht leer sein.");
        }

        String name = shop.getName().trim();
        if (shopRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("Ein Shop mit dem Namen " + name + " existiert bereits.");
        }
        shop.setName(name);

        return shopRepository.save(shop);
    }
}
