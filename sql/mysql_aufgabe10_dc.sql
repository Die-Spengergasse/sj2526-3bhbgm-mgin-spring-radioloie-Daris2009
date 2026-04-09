-- MySQL schema for Radiologie reservation system
-- Database: Aufgabe10_DC

DROP DATABASE IF EXISTS `Aufgabe10_DC`;
CREATE DATABASE `Aufgabe10_DC` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `Aufgabe10_DC`;

-- Patients table
CREATE TABLE patients (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  svnr BIGINT NOT NULL UNIQUE,
  vorname VARCHAR(100) NOT NULL,
  nachname VARCHAR(100) NOT NULL,
  gender ENUM('M','F','D','X') NOT NULL,
  birth DATE NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Devices table
CREATE TABLE devices (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  bezeichnung VARCHAR(200) NOT NULL,
  geraetetyp VARCHAR(50) NOT NULL,
  standort VARCHAR(50) NOT NULL,
  UNIQUE KEY ux_devices_bezeichnung (bezeichnung)
) ENGINE=InnoDB;

-- Body regions lookup table (for selection field)
CREATE TABLE body_regions (
  id SMALLINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(50) NOT NULL UNIQUE,
  description VARCHAR(200) NOT NULL
) ENGINE=InnoDB;

INSERT INTO body_regions (code, description) VALUES
  ('HEAD','Head / Neck'),
  ('CHEST','Chest / Thorax'),
  ('ABDOMEN','Abdomen / Pelvis'),
  ('EXTREMITY','Extremity'),
  ('SPINE','Spine');

-- Reservations table
CREATE TABLE reservations (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patient_id BIGINT NOT NULL,
  device_id BIGINT NOT NULL,
  body_region_id SMALLINT NOT NULL,
  start_time DATETIME NOT NULL,
  end_time DATETIME NOT NULL,
  comment TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_res_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_res_device FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_res_bodyregion FOREIGN KEY (body_region_id) REFERENCES body_regions(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT chk_res_time CHECK (end_time > start_time)
) ENGINE=InnoDB;

-- Helpful index for device schedules
CREATE INDEX idx_res_device_start_end ON reservations (device_id, start_time, end_time);

-- Example seed data
INSERT INTO patients (svnr, vorname, nachname, gender, birth) VALUES
  (1234567890,'Anna','Muster','F','1990-05-12'),
  (9876543210,'Peter','Beispiel','M','1978-11-03');

INSERT INTO devices (bezeichnung, geraetetyp, standort) VALUES
  ('Siemens Magnetom A1','MR','Raum 101'),
  ('Philips CT 256','CT','Raum 102'),
  ('Siemens Multix','Röntgen','Raum 103');

INSERT INTO reservations (patient_id, device_id, body_region_id, start_time, end_time, comment) VALUES
  (1, 1, 1, '2026-04-01 09:00:00', '2026-04-01 09:30:00', 'Routineuntersuchung'),
  (2, 2, 3, '2026-04-01 10:00:00', '2026-04-01 10:20:00', 'Kontrastmittel');

-- Notes:
-- - The CHECK constraint (chk_res_time) requires MySQL 8.0.16+ for enforcement.
-- - Adjust field lengths and types to your needs.
-- - Use a transaction or tooling for bulk imports in production.
