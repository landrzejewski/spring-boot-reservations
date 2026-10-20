# Hotel – rezerwacje pokoi (projekt ćwiczeniowy)

Projekt do samodzielnego przećwiczenia koncepcji ze szkolenia Spring Boot na nowej domenie.
Każdy etap to osobny branch. Zaczynasz od `01-Application`; na każdym branchu w pliku `zadanie.md`
jest opis kolejnego kroku, a jego rozwiązanie znajduje się na następnym branchu.

| Branch | Zawartość | Zadanie (`zadanie.md`) prowadzi do |
|---|---|---|
| `01-Application` | czysta Java, architektura heksagonalna | `02-Spring-Boot-DI-annotation` |
| `02-Spring-Boot-DI-annotation` | Spring Boot, DI przez adnotacje | `02-Spring-Boot-DI-java-config` |
| `02-Spring-Boot-DI-java-config` | DI przez konfigurację Java | `03-Spring-Boot-AOP` |
| `03-Spring-Boot-AOP` | aspekty | `04-Spring-Data` |
| `04-Spring-Data` | JPA + MongoDB | `05-Spring-Web-MVC` |
| `05-Spring-Web-MVC` | REST API | zadania dodatkowe |

## Domena
System rezerwacji pokoi hotelowych.

**Pokój (`Room`)** – agregat:
* `id` – `RoomId` (UUID)
* `number` – `RoomNumber` (3–5 cyfr, np. `101`)
* `type` – `RoomType`: `SINGLE` (1 os.), `DOUBLE` (2 os.), `SUITE` (4 os.)
* `pricePerNight` – `Money` (kwota + waluta)
* `reservations` – lista rezerwacji
* `revenue` – suma wartości wszystkich rezerwacji

**Rezerwacja (`Reservation`)**:
* `id` – `ReservationId` (UUID)
* `timestamp` – moment złożenia rezerwacji
* `period` – `StayPeriod` (`checkIn`, `checkOut`; wyjazd musi być po przyjeździe)
* `guests` – liczba gości (> 0)
* `value` – cena za noc × liczba nocy

**Reguły rejestracji rezerwacji (`Room.registerReservation`)**:
1. Nie można zarezerwować pobytu rozpoczynającego się przed dniem złożenia rezerwacji → `InvalidReservationPeriodException`
2. Liczba gości nie może przekraczać pojemności pokoju → `RoomCapacityExceededException`
3. Termin nie może kolidować z istniejącą rezerwacją → `RoomNotAvailableException`
4. Po zarejestrowaniu rezerwacji pokój publikuje zdarzenie domenowe `ReservationRegistered`

## Przypadki użycia (warstwa `application`)
* `AddRoomUseCase` – dodanie pokoju; numer nadaje `RoomNumberGenerator`:
  * pokoje `SINGLE` / `DOUBLE` – generator piętrowy (`FloorRoomNumberGenerator`: 101, 102, …, 110, 201, …)
  * apartamenty `SUITE` – generator sekwencyjny (`SequentialRoomNumberGenerator`: 900, 901, …)
* `AddReservationUseCase` – dodanie rezerwacji do pokoju, przekształcenie zdarzenia domenowego w `ReservationAdded` i publikacja przez `ReservationEventPublisher`
* `GetRoomUseCase` – pobranie pokoju po numerze (`RoomNotFoundException`, jeśli nie istnieje)
* `GetRoomsUseCase` – stronicowana lista pokoi (`PageSpec` / `ResultPage`)

## Architektura
```
pl.training.hotel
├── domain             – model domenowy, bez zależności zewnętrznych
├── application        – przypadki użycia + porty (RoomRepository, RoomNumberGenerator, DateTimeProvider, ReservationEventPublisher)
├── adapters           – implementacje portów (repozytorium, generatory numerów, publikacja zdarzeń, ...)
└── HotelConfiguration – składanie zależności
```

Na branchu `01-Application` zależności są składane ręcznie w `HotelConfiguration`, a `AddReservationAuditProxy`
(klasa dziedzicząca po `AddReservationUseCase`) zapisuje wpisy audytowe – to wzorzec proxy, który w etapie AOP zostanie zastąpiony aspektem.
Uruchomienie etapu 01:
```
mvn compile && java -cp target/classes pl.training.Application
```
