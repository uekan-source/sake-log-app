/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

// Resolves ixias-v3 from its private S3 Maven repository (needs AWS credentials).
addSbtPlugin("com.frugalmechanic" % "fm-sbt-s3-resolver" % "0.21.0")
classpathTypes += "maven-plugin"
