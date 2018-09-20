name := """vinego-api"""
organization := "org.gibson"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.4"


lazy val h2database = "com.h2database" % "h2" % "1.4.197"

libraryDependencies ++= Seq(
  guice,
  ws,
  evolutions,
  jdbc,
  javaJdbc,
  "mysql" % "mysql-connector-java" % "5.1.41",
  javaJdbc % Test,
  h2database,
  h2database % Test,
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.9.6",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.9.6",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.6",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % "2.9.6",
  "com.netflix.governator" % "governator" % "1.17.5" % "runtime",
  "org.redisson" % "redisson" % "3.7.5",
  "com.google.inject.extensions" % "guice-multibindings" % "4.1.0",
  "com.auth0" % "java-jwt" % "3.4.0"



)

