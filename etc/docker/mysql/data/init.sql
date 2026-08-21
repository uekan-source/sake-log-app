--
-- Copyright IxiaS, Inc. All Rights Reserved.
--
-- Runs once on first container start (docker-entrypoint-initdb.d).
-- Creates the application database and user used by application.conf.
--

CREATE DATABASE IF NOT EXISTS `app` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'app'@'%' IDENTIFIED BY 'pass';
GRANT ALL PRIVILEGES ON `app`.* TO 'app'@'%';
FLUSH PRIVILEGES;
