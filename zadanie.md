# Etap 04 – Spring Data (JPA + MongoDB)

Punkt wyjścia: ten branch (`03-Spring-Boot-AOP`). Rozwiązanie: branch `04-Spring-Data`.

## Zadanie
1. Zależności: `spring-boot-starter-data-jpa`, `spring-boot-starter-data-mongodb`, sterowniki PostgreSQL i H2.
   Bazy uruchomisz przez `docker compose up -d` (PostgreSQL + MongoDB); profil `dev` używa plikowej bazy H2.
2. Usuń `HashMapRoomRepository` – implementacją portu `RoomRepository` zostaje adapter JPA,
   a w profilu `mongo` adapter MongoDB (`@Profile("!mongo")` / `@Profile("mongo")`).
   W `HotelConfiguration` przypadki użycia dostają `RoomRepository` jako parametr metody `@Bean`.
3. **Model JPA** (`adapters.persistence.jpa`):
   * `RoomEntity` – `@Table`, `@Enumerated(STRING)` dla typu, `@Version` (optymistyczne blokowanie),
     `@Embedded MoneyEmbeddable` dla ceny i przychodu z `@AttributeOverride` nazw kolumn,
     audyt Spring Data – `@CreatedDate`, `@LastModifiedDate` + `@EnableJpaAuditing`
   * `ReservationEntity` – osobna tabela, relacja dwukierunkowa `@OneToMany(mappedBy, cascade, orphanRemoval)` / `@ManyToOne(LAZY)`, `@OrderBy`
   * `JpaRoomRepositoryMapper` – aktualizuje istniejącą encję (dodaje tylko nowe rezerwacje) zamiast tworzyć nową przy każdym zapisie
4. **Zapytania** (`JpaRoomRepository`):
   * `findByNumber` z ad-hoc `@EntityGraph(attributePaths = "reservations")`
   * zapytania pochodne: `findByType(type, Pageable)`, `countByType`
   * `findAvailable(type, checkIn, checkOut)` – JPQL z `not exists` (wolne pokoje w terminie)
   * `findOccupancy()` – raport `RoomOccupancy` (wyrażenie konstruktorowe, `left join`, `group by`)
   * `sumRevenue(currency)` – zapytanie natywne
5. **Transakcje** – `@Transactional` na przypadkach użycia (`readOnly = true` dla odczytów).
6. **MongoDB** (`adapters.persistence.mongo`) – dokumenty jako rekordy z zagnieżdżonymi rezerwacjami,
   `BigDecimal` zapisany jako `DECIMAL128`, adapter oparty o `MongoTemplate` (`Query`, `Criteria`, stronicowanie).
7. Przykłady w `JpaExamples` / `MongoExamples` (`ApplicationRunner` uruchamiany po `HotelDemo`):
   wolne pokoje, stronicowanie z sortowaniem, raport obłożenia, przychód, daty audytu,
   konflikt wersji (`ObjectOptimisticLockingFailureException`), `elemMatch` na zagnieżdżonych dokumentach, `updateMulti`.

## Do sprawdzenia
* Jakie zapytania SQL generuje Hibernate (`spring.jpa.show-sql: true`)? Gdzie pojawia się problem N+1?
* Dlaczego wersja pokoju rośnie po dodaniu rezerwacji? Co by się stało bez kolumny przychodu?
* Co się zmieni, gdy z `findByNumber` usuniesz `@EntityGraph`?

## Uruchomienie
```
docker compose up -d
mvn spring-boot:run                                         # PostgreSQL
mvn spring-boot:run -Dspring-boot.run.profiles=dev          # H2
mvn spring-boot:run -Dspring-boot.run.profiles=dev,mongo    # MongoDB (dane w kontenerze zostają między uruchomieniami)
```
