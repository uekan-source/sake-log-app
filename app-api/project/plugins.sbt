/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

addSbtPlugin("org.playframework"     % "sbt-plugin"         % "3.0.8")
// Resolves ixias-v3 from its private S3 Maven repository (needs AWS credentials).
addSbtPlugin("com.frugalmechanic"    % "fm-sbt-s3-resolver" % "0.21.0")
// Flyway schema migrations (reads SQL from etc/database via `sbt flywayMigrate`).
addSbtPlugin("io.github.davidmweber" % "flyway-sbt"         % "7.4.0")
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.33"

// IDE / Metals support (optional but convenient during training).
addSbtPlugin("ch.epfl.scala" % "sbt-bloop"  % "2.0.10")
addSbtPlugin("org.scalameta" % "sbt-metals" % "1.5.3")

classpathTypes += "maven-plugin"
