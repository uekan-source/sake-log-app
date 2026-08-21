--
-- Copyright IxiaS, Inc. All Rights Reserved.
--
-- Simple email + password user registration with server-side sessions.
-- Profile (udb_user), credential (udb_user_password), session (udb_user_session)
-- are kept in separate tables.
--

CREATE TABLE `udb_user` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `uuid`       VARCHAR(64)  NOT NULL,
  `email`      VARCHAR(255) NOT NULL,
  `name`       VARCHAR(255) NOT NULL,
  `state`      SMALLINT     NOT NULL DEFAULT 1,
  `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ukey01` (`uuid`),
  UNIQUE KEY `ukey02` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `udb_user_password` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `uid`        BIGINT UNSIGNED NOT NULL,
  `hash`       VARCHAR(255) NOT NULL,
  `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ukey01` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `udb_user_session` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `uid`        BIGINT UNSIGNED NOT NULL,
  `token`      VARCHAR(255) NOT NULL,
  `state`      SMALLINT     NOT NULL DEFAULT 1,
  `expires_at` DATETIME     NOT NULL,
  `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ukey01` (`token`),
  KEY `key01` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
