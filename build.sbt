name := """play-ground"""
organization := "pl.net.kopczynski"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += ws

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "pl.net.kopczynski.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "pl.net.kopczynski.binders._"
