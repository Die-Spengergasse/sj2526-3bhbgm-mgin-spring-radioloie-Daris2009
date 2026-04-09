MySQL setup for Radiologie reservation system (Aufgabe10_DC)

This README explains how to create a MySQL database `Aufgabe10_DC`, create a user, import the provided SQL schema (in `sql/mysql_aufgabe10_dc.sql`) and run the Spring Boot app using MySQL.

1) Start MySQL server (ensure MySQL 8+ is installed).

2) Create the database and a user (example):

-- create user (run in mysql client):
CREATE DATABASE Aufgabe10_DC CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'radiuser'@'localhost' IDENTIFIED BY 'radiPassword123!';
GRANT ALL PRIVILEGES ON Aufgabe10_DC.* TO 'radiuser'@'localhost';
FLUSH PRIVILEGES;

3) Import the SQL schema file (from project root):

# Windows (PowerShell)
mysql -u root -p < sql\mysql_aufgabe10_dc.sql

# Or import using the created user
mysql -u radiuser -p Aufgabe10_DC < sql\mysql_aufgabe10_dc.sql

4) Configure Spring to use the MySQL profile:

- Edit `src/main/resources/application-mysql.properties` and set `spring.datasource.username` and `spring.datasource.password` to your DB user/password if not using root.

- Start the application with the mysql profile active:

./mvnw -Dspring-boot.run.profiles=mysql -DskipTests spring-boot:run

(Or pass as JVM property: -Dspring.profiles.active=mysql)

5) Verify:
- Connect to the DB via MySQL client: SELECT * FROM patients; SELECT * FROM reservations;
- The application will use the MySQL database if profile `mysql` is active.

Notes and troubleshooting
- If Hibernate reports missing tables when `spring.jpa.hibernate.ddl-auto=validate`, set it temporarily to `update` to let Hibernate create tables for you; but for a production-like setup you should import the SQL file and leave `validate`.
- The SQL file includes seeds for patients/devices/reservations; adjust as needed.

