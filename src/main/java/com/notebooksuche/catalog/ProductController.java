package com.notebooksuche.catalog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/test")
    public String checkSystem() {
        return "Hallo! Der Kern des Notebooksuche-Aggregators wurde erfolgreich gestartet. Der Server läuft!";
    }
}