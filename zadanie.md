# Etap 03 – Spring AOP

Punkt wyjścia: ten branch (`02-Spring-Boot-DI-java-config`). Rozwiązanie: branch `03-Spring-Boot-AOP`.

## Zadanie
1. Dodaj zależność `spring-boot-starter-aspectj`.
2. **Audyt** (`pl.training.common.audit`) – zastępuje `AddReservationAuditProxy` z etapu 01:
   * adnotacja `@Audited("NAZWA_AKCJI")`, rekord `AuditEntry` (czas, akcja, wynik `SUCCESS`/`FAILURE`, szczegóły), bean `AuditLog` przechowujący wpisy
   * `AuditAspect` (`@Order(1)`):
     * nazwany pointcut `applicationLayer()` – `execution(public * pl.training.hotel.application..*(..))`
     * `@AfterReturning` – połączenie `applicationLayer() && @annotation(audited)`, wiązanie adnotacji i zwróconej wartości (`returning`)
     * `@AfterThrowing` – wiązanie wyjątku (`throwing`)
   * oznacz `AddRoomUseCase.handle` (`ADD_ROOM`) i `AddReservationUseCase.handle` (`ADD_RESERVATION`)
3. **Polityka pobytu** (`pl.training.hotel.adapters`):
   * `StayPolicyProperties` (`hotel.stay-policy.max-nights: 14`)
   * `StayPolicyAspect` – `@Before` na `AddReservationUseCase.handle` z wiązaniem argumentów `args(roomNumber, stayPeriod, guests)`;
     zbyt długi pobyt → `StayPolicyViolationException`
   * aspekt zarejestruj jako zwykły `@Bean` w `HotelConfiguration` (klasa ma tylko `@Aspect`, bez `@Component`)
4. **Ograniczanie liczby wywołań** (`pl.training.common.aop`):
   * `@Throttled(permits, periodInSeconds)` + `ThrottlingAspect` (`@Around`) – przesuwne okno czasowe per metoda, po przekroczeniu limitu `ThrottlingException`
   * oznacz `GetRoomsUseCase.handle` – `@Throttled(permits = 3, periodInSeconds = 1)`
5. W `HotelDemo` pokaż: rezerwację kolidującą, zbyt długi pobyt, przekroczenie limitu wywołań i liczbę wpisów audytu.

## Do sprawdzenia
* Czy próba zbyt długiego pobytu trafia do audytu? Od czego to zależy (kolejność aspektów)?
* Dlaczego aspekt nie zadziała dla metody prywatnej ani dla wywołania `this.handle(...)` wewnątrz tej samej klasy?
* Zmień pointcut `applicationLayer()` tak, aby obejmował tylko klasy, których nazwa kończy się na `UseCase`.

## Uruchomienie
```
mvn spring-boot:run
```
