CREATE TABLE users (username VARCHAR(50) NOT NULL PRIMARY KEY,password VARCHAR(100) NOT NULL,enabled BOOLEAN NOT NULL);

CREATE TABLE authorities (username VARCHAR(50) NOT NULL,authority VARCHAR(50) NOT NULL);

ALTER TABLE authorities ADD CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users(username);

INSERT INTO users (username, password, enabled) VALUES ('jan', '$2a$10$nmZB2dvjw.YbIRirM5WBXOrZy0YN0nyUTl/nTxDbOLSh2AZ6qYuQa', TRUE);

INSERT INTO authorities (username, authority) VALUES ('jan', 'ROLE_ADMIN');