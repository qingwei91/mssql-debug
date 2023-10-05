ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "mssql-debug",
    libraryDependencies += "com.microsoft.sqlserver" % "mssql-jdbc" % "12.4.0.jre11"
  )
