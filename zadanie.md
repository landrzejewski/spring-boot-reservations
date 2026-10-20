# Etap 02 – Spring Boot: wstrzykiwanie zależności (adnotacje)

Punkt wyjścia: ten branch (`01-Application`). Rozwiązanie: branch `02-Spring-Boot-DI-annotation`.

## Zadanie
1. Przekształć projekt w aplikację Spring Boot (`spring-boot-starter-parent`, `spring-boot-starter`, `spring-boot-maven-plugin`).
2. Usuń `HotelConfiguration` i `AddReservationAuditProxy` – beany mają być wykrywane przez skanowanie komponentów
   (`@Service` dla przypadków użycia, `@Repository` dla repozytorium, `@Component` dla pozostałych adapterów).
3. Konfiguracja numeracji pokoi ma trafić do `application.yml` i zostać zmapowana na rekord
   `RoomNumberProperties` (`@ConfigurationProperties("hotel.room-numbers")`, rejestracja przez `@ConfigurationPropertiesScan`):
   ```yaml
   hotel:
     room-numbers:
       rooms-per-floor: 10
       suite-start-number: 900
   ```
   Generatory mają dwa konstruktory (z `int` i z `RoomNumberProperties`) – wskaż Springowi właściwy przez `@Autowired`.
4. Wybór generatora w `AddRoomUseCase`:
   * `FloorRoomNumberGenerator` – `@Primary` (trafia do parametru bez kwalifikatora)
   * `SequentialRoomNumberGenerator` – bean o nazwie `suiteRoomNumberGenerator`, wstrzykiwany przez `@Qualifier`
5. Dodaj drugi adapter zdarzeń – `InMemoryReservationEventPublisher` (przechowuje historię zdarzeń).
   `AddReservationUseCase` ma przyjmować `List<ReservationEventPublisher>` i publikować do wszystkich; kolejność ustal przez `@Order`.
6. Dodaj metody cyklu życia `@PostConstruct` / `@PreDestroy` w `HashMapRoomRepository`.
7. `Application` zawiera tylko `main`; scenariusz demonstracyjny przenieś do osobnego beana `HotelDemo implements CommandLineRunner`
   (na końcu wypisz historię zdarzeń z `InMemoryReservationEventPublisher`).

## Do sprawdzenia
* Co się stanie, gdy usuniesz `@Primary` z `FloorRoomNumberGenerator`? A gdy usuniesz `@Autowired` z konstruktora generatora?
* Zmień `@Order` publisherów – czy zmieniła się kolejność publikacji?

## Uruchomienie
```
mvn spring-boot:run
```
