import sbtdocker.DockerKeys._


name := """HData-SI-RH"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.nimbusds" % "nimbus-jose-jwt" % "4.12",
  "org.apache.hbase" % "hbase-shaded-client" % "1.2.0",
  "org.apache.hbase" % "hbase" % "1.2.0",
  "org.apache.hbase" % "hbase-common" % "1.2.0",
  "org.apache.hadoop" % "hadoop-common" % "2.7.1",
  "commons-logging" % "commons-logging" % "1.2"
)


