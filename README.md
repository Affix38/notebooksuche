# notebooksuche – Preisvergleich für Notebooks

Eigenes Backend-Projekt: ein Preisvergleichsportal für Notebooks nach dem Vorbild von idealo.de.
Der Katalog-Service bietet eine REST-API, mit der Notebooks und Shops verwaltet,
Angebote erfasst und die Preise mehrerer Shops für ein Notebook verglichen werden können.

## Technologie

| Bereich | Technik |
|---|---|
| Sprache | Java 25 |
| Framework | Spring Boot 4.1.1 (Web MVC, Data JPA) |
| Datenbank | PostgreSQL 18, Zugriff über Hibernate/JPA |
| Build | Maven (Maven Wrapper) |
| Tests (manuell) | IntelliJ HTTP Client (`api-tests.http`), Postman |

## Architektur

Dreischichtiger Aufbau: **Controller → Service → Repository → PostgreSQL**.

- **Controller** nehmen HTTP-Anfragen entgegen und liefern JSON.
- **Services** enthalten die Geschäftsregeln (Validierung, Transaktionen).
- **Repositories** (Spring Data JPA) kapseln den Datenbankzugriff.
- **Entitäten:** `Notebook`, `Shop`, `Angebot` (Beziehungen nur unidirektional per `@ManyToOne`).
- **DTOs als Java Records:** `AngebotDto`, `AngebotAnlegenDto`, `PreisvergleichDto`.
  Die API gibt für den Preisvergleich keine Entitäten zurück.
- **Zentrale Fehlerbehandlung** (`@RestControllerAdvice`): `NichtGefundenException` → 404,
  `IllegalArgumentException` → 400.

Wichtige Entwurfsentscheidungen:

- Kein `@OneToMany` in `Notebook` und `Shop`: vermeidet JSON-Rekursion und unbeabsichtigtes Lazy Loading.
  Angebote werden gezielt über `AngebotRepository` geladen.
- Records nur für DTOs, nie für JPA-Entitäten (Hibernate benötigt Standardkonstruktor und Proxies).
- Pro Notebook darf jeder Shop nur ein Angebot haben (Prüfung im Service; Datenbank-Constraint folgt mit Flyway).
- Datenbank-Zugangsdaten kommen ausschließlich aus Umgebungsvariablen, nie aus dem Repository.

## API

| Methode | Pfad | Beschreibung |
|---|---|---|
| GET | `/api/notebooks` | alle Notebooks |
| GET | `/api/notebooks/{id}` | ein Notebook |
| GET | `/api/notebooks/suche?marke=...` | Suche nach Marke |
| POST | `/api/notebooks` | Notebook anlegen |
| GET | `/api/notebooks/{id}/angebote` | Angebote, günstigstes zuerst |
| GET | `/api/notebooks/{id}/preisvergleich` | Stammdaten, günstigster Preis, alle Angebote |
| GET / POST | `/api/shops`, `/api/shops/{id}` | Shops verwalten |
| POST | `/api/angebote` | Angebot anlegen |
| POST | `/api/notebooks/beispieldaten`, `/api/angebote/beispieldaten` | Beispieldaten erzeugen |

Beispiele für alle Aufrufe (inklusive Fehlerfälle) stehen in [`api-tests.http`](api-tests.http).

## Lokal starten

Voraussetzungen: JDK 25, PostgreSQL 18 mit einer leeren Datenbank `notebooksuche`.

```powershell
# Zugangsdaten (DB_USER und DB_URL sind optional, Standard: postgres bzw. localhost:5432)
$env:DB_PASSWORD = "<Ihr Passwort>"

.\mvnw spring-boot:run
```

Danach z. B. `POST http://localhost:8080/api/notebooks/beispieldaten` und
`POST http://localhost:8080/api/angebote/beispieldaten` aufrufen und
`GET http://localhost:8080/api/notebooks/1/preisvergleich` testen.

## Stand und Roadmap

Umgesetzt: Katalog, Shops, Angebote, Preisvergleich, Fehlerbehandlung.

In Arbeit (Stufe 0 – Grundlagen): Flyway-Migrationen und Datenbank-Constraint,
Bean Validation, automatisierte Tests (JUnit, Mockito, Testcontainers), CI mit GitHub Actions.

Geplant: strukturierte Produktdaten und Preisverlauf, Import von Händler-Feeds (EAN-Matching),
Suche mit OpenSearch, Frontend, Klick-Weiterleitung, Benutzerkonten und Preisalarme,
Containerisierung (Docker, Kubernetes).

## Entstehung

Projektidee, Funktionsumfang und Architektur stammen von mir. Beim Schreiben von Code
hat mich der KI-Assistent Claude Code unterstützt. Jede Funktion habe ich manuell getestet
(siehe `api-tests.http`); beim Testen habe ich unter anderem den Fehler mit doppelten Angeboten
desselben Shops gefunden.
