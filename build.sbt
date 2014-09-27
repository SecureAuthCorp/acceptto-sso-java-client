name := """acceptto-sso-java-client"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

resolvers ++= Seq(
  "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.pac4j" % "play-pac4j_java" % "1.3.0-SNAPSHOT",
  "org.pac4j" % "pac4j-cas" % "1.6.0-SNAPSHOT"
)
