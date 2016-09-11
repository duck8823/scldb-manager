name := "scldbc"
version := "0.0.1"

scalaVersion := "2.11.8"
libraryDependencies += "org.jdbi" % "jdbi" % "2.75"
libraryDependencies += "com.zaxxer" % "HikariCP" % "2.4.7"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.8.11.2"
libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1206-jdbc42"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

