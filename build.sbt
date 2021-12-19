
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

mainClass := Some("ru.school21.turing.Main")