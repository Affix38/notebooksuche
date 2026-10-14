package com.notebooksuche.catalog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-Controller für die Verwaltung von Partnershops.
 * Delegiert Geschäftsvorgänge an den ShopService.
 */
@RestController
@RequestMapping("/api/shops")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    /**
     * Gibt alle Shops zurück.
     * Endpunkt: GET /api/shops
     */
    @GetMapping
    public List<Shop> alleShopsAbrufen() {
        return shopService.alleShopsFinden();
    }

    /**
     * Gibt einen Shop anhand seiner ID zurück (404, falls nicht vorhanden).
     * Endpunkt: GET /api/shops/{id}
     */
    @GetMapping("/{id}")
    public Shop shopNachIdAbrufen(@PathVariable Long id) {
        return shopService.nachIdFinden(id);
    }

    /**
     * Legt einen neuen Shop an (400 bei Validierungsfehlern).
     * Endpunkt: POST /api/shops
     */
    @PostMapping
    public ResponseEntity<Shop> shopErstellen(@RequestBody Shop shop) {
        Shop gespeicherterShop = shopService.shopSpeichern(shop);
        return ResponseEntity.status(HttpStatus.CREATED).body(gespeicherterShop);
    }
}
