CREATE
DATABASE sportify_test;
USE sportify_test;

CREATE TABLE IF NOT EXISTS Users
(
    email       VARCHAR(50) NOT NULL,
    username    VARCHAR(50) PRIMARY KEY,
    realname    VARCHAR(50) NOT NULL,
    realsurname VARCHAR(50) NOT NULL,
    password    VARCHAR(50) NOT NULL
);


CREATE TABLE IF NOT EXISTS Statistics
(
    `id`       INTEGER PRIMARY KEY,
    `Div`      VARCHAR(5)   NOT NULL,
    `Date`     VARCHAR(10)  NOT NULL,
    `HomeTeam` VARCHAR(50)  NOT NULL,
    `AwayTeam` VARCHAR(50)  NOT NULL,
    `FTHG`     INTEGER      NOT NULL,
    `FTAG`     INTEGER      NOT NULL,
    `FTR`      CHARACTER(1) NOT NULL,
    `HTHG`     INTEGER      NOT NULL,
    `HTAG`     INTEGER      NOT NULL,
    `HTR`      CHARACTER(1) NOT NULL,
    `Referee`  VARCHAR(50)  NOT NULL
);

CREATE TABLE IF NOT EXISTS Standings
(
    `id`         INTEGER PRIMARY KEY,
    `division`   VARCHAR(5)  NOT NULL,
    `name`       VARCHAR(50) NOT NULL,
    `position`   INTEGER     NULL,
    `P`          INTEGER     NOT NULL,
    `W`          INTEGER     NOT NULL,
    `D`          INTEGER     NOT NULL,
    `L`          INTEGER     NOT NULL,
    `GF`         INTEGER     NOT NULL,
    `GA`         INTEGER     NOT NULL,
    `GD`         INTEGER     NOT NULL,
    `points`     INTEGER     NOT NULL,
    `pastPoints` INTEGER     NOT NULL,
    `movement`   INTEGER     NULL
);

CREATE TABLE IF NOT EXISTS Matches
(
    `id`       INTEGER PRIMARY KEY,
    `division` VARCHAR(5)  NOT NULL,
    `date`     VARCHAR(50) NOT NULL,
    `HomeTeam` VARCHAR(50) NOT NULL,
    `AwayTeam` VARCHAR(50) NOT NULL,
    `B365H`    REAL        NOT NULL,
    `B365D`    REAL        NOT NULL,
    `B365A`    REAL        NOT NULL
);