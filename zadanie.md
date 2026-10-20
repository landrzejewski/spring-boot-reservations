# Etap 05 – Spring Web MVC

Punkt wyjścia: ten branch (`04-Spring-Data`). Rozwiązanie: branch `05-Spring-Web-MVC`.

## Zadanie
1. Zależności: `spring-boot-starter-webmvc`, `spring-boot-starter-validation`, `spring-boot-starter-actuator`, `springdoc-openapi-starter-webmvc-ui`.
2. Usuń `HotelDemo`; przykłady Spring Data uruchamiaj tylko w profilu `examples` (`@Profile("examples & !mongo")`).
3. **API REST** (`pl.training.hotel.adapters.rest`, DTO jako rekordy w osobnych plikach):

   | Metoda | Ścieżka | Wynik |
   |---|---|---|
   | `POST` | `/api/rooms` | `201` + nagłówek `Location` (zbudowany przez wstrzyknięty `UriComponentsBuilder`) |
   | `GET` | `/api/rooms/{number}` | pokój z rezerwacjami |
   | `GET` | `/api/rooms?page=&size=` | strona pokoi (`ResultPage`) |
   | `POST` | `/api/rooms/{number}/reservations` | `201` + id rezerwacji (`@ResponseStatus`) |

4. **Walidacja**:
   * Bean Validation w żądaniach (`@NotNull`, `@DecimalMin`/`@DecimalMax`, `@Pattern`, `@FutureOrPresent`, `@Min`)
   * własne ograniczenie na poziomie klasy `@ValidDateRange` (`common.validation`) – sprawdza, czy wyjazd jest po przyjeździe;
     działa dla każdego typu implementującego interfejs `DateRange`
   * walidacja parametrów metod kontrolera: `@Pattern` na `@PathVariable`, `@Min`/`@Max` na `@RequestParam`
5. **Obsługa błędów – RFC 7807** (`HotelRestExceptionHandler extends ResponseEntityExceptionHandler`):
   * odpowiedzi `application/problem+json` (`ProblemDetail`) z dodatkowymi polami `timestamp` i `errors`
   * mapowanie: `RoomNotFoundException` → 404, `RoomNotAvailableException` → 409,
     `RoomCapacityExceededException` / `InvalidReservationPeriodException` / `StayPolicyViolationException` → 422,
     `ThrottlingException` → 429 + nagłówek `Retry-After`
   * tytuły i opisy z `messages.properties` / `messages_pl.properties` (klucze `problemDetail.title.<klasa>` i `problemDetail.<klasa>`),
     język wg `Accept-Language` (`spring.messages.fallback-to-system-locale: false`)
6. **Interceptor** – `RequestTimingInterceptor` (`HandlerInterceptor`) dodaje nagłówek `X-Request-Id` i loguje czas obsługi żądania;
   zarejestruj go dla `/api/**` w `WebConfiguration implements WebMvcConfigurer`.
7. **Actuator** – udostępnij `health` i `info`; `HotelInfoContributor` dodaje do `/actuator/info` konfigurację hotelu.
8. **OpenAPI** – opisz kontrolery adnotacjami `@Tag` i `@Operation` (`/swagger-ui.html`).
9. Scenariusz testowy w `client.http`.

## Do sprawdzenia
* Kiedy Spring zgłasza `MethodArgumentNotValidException`, a kiedy `HandlerMethodValidationException`?
* Wyślij kilka szybkich żądań `GET /api/rooms` – co zwraca serwer i dlaczego?
* Porównaj odpowiedzi błędów z `Accept-Language: pl-PL` i `en`.

## Uruchomienie
```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
mvn spring-boot:run -Dspring-boot.run.profiles=dev,examples
```
