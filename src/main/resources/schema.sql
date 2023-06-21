drop table IF EXISTS mpa cascade;
drop table IF EXISTS films cascade;
drop table IF EXISTS genres cascade;
drop table IF EXISTS filmgenres cascade;
drop table IF EXISTS users cascade;
drop table IF EXISTS friends cascade;
drop table IF EXISTS likes cascade;

create table IF NOT EXISTS mpa
(
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR(200) NOT NULL unique
);

create TABLE IF NOT EXISTS films
(
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  description VARCHAR(200),
  releaseDate DATE NOT NULL,
  duration INTEGER NOT NULL,
  mpaid INTEGER NOT NULL REFERENCES mpa(id) ON delete RESTRICT
);

create TABLE IF NOT EXISTS genres
(
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR(200) NOT NULL unique
);

create TABLE IF NOT EXISTS filmgenres
(
  filmid BIGINT REFERENCES films(id) ON delete CASCADE,
  genreid INTEGER REFERENCES genres(id) ON delete RESTRICT,
  primary key (filmid, genreid)
);

create TABLE IF NOT EXISTS users
(
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  email VARCHAR(200) NOT NULL,
  login VARCHAR(200) NOT NULL,
  name VARCHAR(200) NOT NULL,
  birthday DATE NOT NULL
);

create TABLE IF NOT EXISTS friends
(
  userid BIGINT REFERENCES users(id) ON delete CASCADE,
  friendid BIGINT REFERENCES users(id) ON delete CASCADE,
  primary key (userid, friendid)
);

create TABLE IF NOT EXISTS likes
(
  userid BIGINT REFERENCES users(id) ON delete CASCADE,
  filmid BIGINT REFERENCES films(id) ON delete CASCADE,
  primary key (userid, filmid)
);

drop table IF EXISTS reviews cascade;
drop table IF EXISTS reviewlikes cascade;

create TABLE IF NOT EXISTS REVIEWS
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(200) NOT NULL,
    ispositive boolean,
    userid BIGINT NOT NULL REFERENCES users(id) ON delete RESTRICT,
    filmid BIGINT NOT NULL REFERENCES films(id) ON delete RESTRICT,
    useful BIGINT DEFAULT 0
);

create TABLE IF NOT EXISTS REVIEWLIKES
(
  userid BIGINT REFERENCES PUBLIC.users(id) ON delete CASCADE,
  reviewid BIGINT REFERENCES PUBLIC.REVIEWS(id) ON delete CASCADE,
  primary key (userid, reviewid)
);

ALTER TABLE IF EXISTS FILMS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE IF EXISTS USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE IF EXISTS REVIEWS ALTER COLUMN ID RESTART WITH 1;

INSERT INTO mpa (id, name) VALUES (1, 'G');
INSERT INTO mpa (id, name) VALUES (2, 'PG');
INSERT INTO mpa (id, name) VALUES (3, 'PG-13');
INSERT INTO mpa (id, name) VALUES (4, 'R');
INSERT INTO mpa (id, name) VALUES (5, 'NC-17');

INSERT INTO genres (id, name) VALUES (1, 'Комедия');
INSERT INTO genres (id, name) VALUES (2, 'Драма');
INSERT INTO genres (id, name) VALUES (3, 'Мультфильм');
INSERT INTO genres (id, name) VALUES (4, 'Триллер');
INSERT INTO genres (id, name) VALUES (5, 'Документальный');
INSERT INTO genres (id, name) VALUES (6, 'Боевик');

