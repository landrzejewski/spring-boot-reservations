# Etap 02 – Spring Boot: wstrzykiwanie zależności (konfiguracja Java)

Punkt wyjścia: ten branch (`02-Spring-Boot-DI-annotation`). Rozwiązanie: branch `02-Spring-Boot-DI-java-config`.

## Zadanie
1. Usuń wszystkie adnotacje Springa z klas w `application` i `adapters` (poza `RoomNumberProperties`) – klasy hotelu mają być czystą Javą.
2. Zdefiniuj wszystkie beany w `pl.training.hotel.HotelConfiguration` (`@Configuration`):
   * `RoomNumberProperties` zarejestruj przez `@EnableConfigurationProperties` (zamiast `@ConfigurationPropertiesScan`)
   * repozytorium – metody cyklu życia wskaż przez `@Bean(initMethod = "init", destroyMethod = "destroy")`
   * generatory – dwa beany typu `RoomNumberGenerator` (`standardRoomNumberGenerator`, `suiteRoomNumberGenerator`) tworzone na podstawie `RoomNumberProperties`
   * przypadki użycia – zależności przekazuj, wywołując inne metody `@Bean` (np. `roomRepository()`);
     sprawdź, że mimo wielokrotnego wywołania powstaje jedna instancja repozytorium
   * publishery zdarzeń – kolejność przez `@Order` na metodach `@Bean`;
     `InMemoryReservationEventPublisher` ma istnieć tylko w profilu `dev` (`@Profile("dev")`)
3. `HotelDemo` pobiera historię zdarzeń przez `ObjectProvider<InMemoryReservationEventPublisher>` – bez profilu `dev` beana nie ma i nic nie jest wypisywane.
4. Dodaj `application-dev.yml` z inną numeracją pokoi (np. 20 pokoi na piętro, apartamenty od 9000) i poziomem logowania `debug`.

## Do sprawdzenia
* Co się stanie, gdy dodasz `@Configuration(proxyBeanMethods = false)`?
* Uruchom aplikację z profilem `dev` i bez niego – porównaj numery apartamentów i historię zdarzeń.

## Uruchomienie
```
mvn spring-boot:run
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
