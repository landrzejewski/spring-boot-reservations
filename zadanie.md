# Zadania dodatkowe

Punkt wyjścia: ten branch (`05-Spring-Web-MVC`). Te zadania nie mają gotowego rozwiązania.

## 1. Anulowanie rezerwacji
* domena: `Room.cancelReservation(ReservationId)` – usuwa rezerwację i koryguje przychód; nieistniejąca rezerwacja → `ReservationNotFoundException`
* przypadek użycia `CancelReservationUseCase` (transakcyjny, objęty audytem `CANCEL_RESERVATION`)
* trwałość: usunięcie rezerwacji musi usuwać wiersz w tabeli `reservations` (`orphanRemoval`) i element w dokumencie Mongo
* REST: `DELETE /api/rooms/{number}/reservations/{id}` → `204`, `404` dla nieistniejącego pokoju / rezerwacji

## 2. Wyszukiwanie wolnych pokoi
* nowa metoda portu `RoomRepository.findAvailable(RoomType, StayPeriod)` i przypadek użycia `FindAvailableRoomsUseCase`
* implementacja JPA na bazie istniejącego zapytania `JpaRoomRepository.findAvailable`, implementacja Mongo przez `MongoTemplate`
* REST: `GET /api/rooms/available?type=DOUBLE&checkIn=...&checkOut=...` – walidacja parametrów (`@DateTimeFormat`, poprawny zakres dat)

## 3. Testy
* testy jednostkowe reguł `Room.registerReservation` (bez Springa)
* `@WebMvcTest` dla `RoomReservationsRestController` – sprawdź kody odpowiedzi i treść `ProblemDetail`
* `@DataJpaTest` dla zapytań `JpaRoomRepository` (wolne pokoje, raport obłożenia)
