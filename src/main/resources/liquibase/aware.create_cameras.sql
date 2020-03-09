-- liquibase formatted sql

-- changeset poofpillow:CREATE_TEST_TABLE logicalFilePath:aware.create_cameras.sql

CREATE TABLE aware.cameras
(
    name          text PRIMARY KEY,
    latitude      double precision NOT NULL,
    longitude     double precision NOT NULL
);
