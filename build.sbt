name := "introduction-to-scalikejdbc"

version := "1.0"

scalaVersion := "2.11.8"

val scalikejdbcVersion = "3.0.0-M4"
libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc",
  "org.scalikejdbc" %% "scalikejdbc-interpolation-macro",
  "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro",
  "org.scalikejdbc" %% "scalikejdbc-config"
).map(_ % scalikejdbcVersion) ++ Seq(
  "com.h2database" % "h2" % "1.4.193",
  "ch.qos.logback" % "logback-classic"  % "1.1.8",
  "org.postgresql" % "postgresql" % "9.4.1212"
)
