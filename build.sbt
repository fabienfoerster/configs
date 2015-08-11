name := "configs"

commonSettings
disablePublishSettings

lazy val configVersion = settingKey[String]("Typesafe config version")

lazy val commonSettings: Seq[Setting[_]] = Seq(
  description := "A Scala wrapper for Typesafe config",
  organization := "com.github.kxbmap",
  scalaVersion := "2.11.7",
  crossScalaVersions += "2.12.0-M2",
  configVersion := "1.3.0",
  scalapropsVersion := "0.1.12",
  scalacOptions ++= Seq(
    "-deprecation",
    "-unchecked",
    "-feature",
    "-Xlint",
    "-Xexperimental",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:experimental.macros"
  )
)

lazy val core = project.settings(
  name := "configs",
  commonSettings,
  scalapropsSettings,
  libraryDependencies ++= Seq(
    "com.typesafe" % "config" % configVersion.value
  )
).dependsOn(
  macros % "provided",
  macros % "provided->provided",
  testkit % "test"
)

lazy val macros = project.settings(
  name := "configs-macro",
  commonSettings,
  scalapropsSettings,
  libraryDependencies ++= Seq(
    "com.typesafe" % "config" % configVersion.value,
    "org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided"
  )
)

lazy val testkit = project.settings(
  name := "configs-testkit",
  commonSettings,
  libraryDependencies ++= Seq(
    "com.typesafe" % "config" % configVersion.value,
    "com.github.scalaprops" %% "scalaprops" % scalapropsVersion.value
  ),
  disablePublishSettings
)
