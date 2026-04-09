-- Example helper SQL to create database and user for the Radiologie app
CREATE DATABASE IF NOT EXISTS `Aufgabe10_DC` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'radiuser'@'localhost' IDENTIFIED BY 'radiPassword123!';
GRANT ALL PRIVILEGES ON `Aufgabe10_DC`.* TO 'radiuser'@'localhost';
FLUSH PRIVILEGES;
-- After creating the user run: mysql -u radiuser -p Aufgabe10_DC < mysql_aufgabe10_dc.sql

