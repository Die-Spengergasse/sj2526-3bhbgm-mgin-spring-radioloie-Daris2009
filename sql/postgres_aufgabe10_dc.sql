-- PostgreSQL schema for Radiologie reservation system
-- Database: Aufgabe10_DC

-- Drop DB objects if they exist
DROP SCHEMA IF EXISTS aufgabe10_dc CASCADE;
CREATE SCHEMA aufgabe10_dc;
SET search_path = aufgabe10_dc;

-- Gender enum
DO $$ BEGIN
    CREATE TYPE gender_t AS ENUM ('M','F','D','X');
EXCEPTION
    WHEN duplicate_object THEN NULL;
END $$;

-- Patients table
CREATE TABLE patients (
  id BIGSERIAL PRIMARY KEY,
  svnr VARCHAR(50) NOT NULL UNIQUE,
  vorname VARCHAR(100) NOT NULL,
  nachname VARCHAR(100) NOT NULL,
  gender gender_t NOT NULL,
  birth DATE NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);

-- Devices table
CREATE TABLE devices (
  id BIGSERIAL PRIMARY KEY,
  bezeichnung VARCHAR(200) NOT NULL,
  geraetetyp VARCHAR(50) NOT NULL,
  standort VARCHAR(50) NOT NULL
);
CREATE UNIQUE INDEX ux_devices_bezeichnung ON devices (bezeichnung);

-- Body regions
CREATE TABLE body_regions (
  id SMALLSERIAL PRIMARY KEY,
  code VARCHAR(50) NOT NULL UNIQUE,
  description VARCHAR(200) NOT NULL
);

INSERT INTO body_regions (code, description) VALUES
  ('HEAD','Head / Neck'),
  ('CHEST','Chest / Thorax'),
  ('ABDOMEN','Abdomen / Pelvis'),
  ('EXTREMITY','Extremity'),
  ('SPINE','Spine');

-- Reservations
CREATE TABLE reservations (
  id BIGSERIAL PRIMARY KEY,
  patient_id BIGINT NOT NULL REFERENCES patients(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  device_id BIGINT NOT NULL REFERENCES devices(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  body_region_id SMALLINT NOT NULL REFERENCES body_regions(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  start_time TIMESTAMP WITH TIME ZONE NOT NULL,
  end_time TIMESTAMP WITH TIME ZONE NOT NULL,
  comment TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
  CHECK (end_time > start_time)
);

CREATE INDEX idx_res_device_start_end ON reservations (device_id, start_time, end_time);

-- Seed data
INSERT INTO patients (svnr, vorname, nachname, gender, birth) VALUES
  ('AT1234567890','Anna','Muster','F','1990-05-12'),
  ('AT0987654321','Peter','Beispiel','M','1978-11-03');

INSERT INTO devices (bezeichnung, geraetetyp, standort) VALUES
  ('Siemens Magnetom A1','MR','Raum 101'),
  ('Philips CT 256','CT','Raum 102'),
  ('Siemens Multix','Röntgen','Raum 103');

INSERT INTO reservations (patient_id, device_id, body_region_id, start_time, end_time, comment) VALUES
  (1, 1, 1, '2026-04-01 09:00:00+01', '2026-04-01 09:30:00+01', 'Routineuntersuchung'),
  (2, 2, 3, '2026-04-01 10:00:00+01', '2026-04-01 10:20:00+01', 'Kontrastmittel');

-- Notes:
-- This schema uses a separate schema 'aufgabe10_dc'. You can change that or copy the objects to public schema if desired.
-- Timestamps include time zone for clarity.

