
name := "ft_turing"

version := "1.0"

scalaVersion := "2.13.6"

libraryDependencies := Seq(
  "org.json4s"       %% "json4s-jackson" % "4.0.3",
  "com.github.scopt" %% "scopt"          % "4.0.1",
  "org.scalatest"    %% "scalatest"      % "3.2.10" % "test")

assemblyMergeStrategy := {
  case "module-info.class" => MergeStrategy.discard
  case x => MergeStrategy.defaultMergeStrategy(x)
}

lazy val common = (project in file(".")).
  settings(
    assembly / logLevel := Level.Warn,
    assembly / mainClass := Some("ru.school21.turing.Main"),
    assembly / assemblyJarName := s"${name.value}.jar",
    )

lazy val res = new File(s"./ft_turing.jar")

lazy val turing = taskKey[Unit]("Create jar for 42")
turing := {
  assembly.value
  IO.move(baseDirectory.value / "target" / "scala-2.13" / "ft_turing.jar", res)
}

lazy val clear = taskKey[Unit]("Clean up")
clear := {
  clean.value
  IO.delete(res)
  IO.delete(baseDirectory.value / "target")
}
