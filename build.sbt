import Dependencies._

ThisBuild / scalaVersion := "2.13.1"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "me.benetis"
ThisBuild / organizationName := "benetis.me"

lazy val root = (project in file("."))
  .settings(
    name := "benetis.me",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.8.2",
    libraryDependencies += "dev.zio" %% "zio" % "1.0.0-RC18"
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
