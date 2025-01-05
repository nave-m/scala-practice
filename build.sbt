ThisBuild / scalaVersion := "2.13.14"
ThisBuild / organization := "com.example"

val toolkitTest = "org.scala-lang" %% "toolkit-test" % "0.1.7"

lazy val hello = project
  .in(file("hello"))
  .aggregate(helloCore)
  .dependsOn(helloCore)
  .settings(
    name := "Hello",
    libraryDependencies += toolkitTest % Test
  )

lazy val helloCore = project
  .in(file("core"))
  .settings(
    name := "Hello Core",
    libraryDependencies += "org.scala-lang" %% "toolkit" % "0.1.7",
    libraryDependencies += toolkitTest % Test
  )

lazy val exampleApplication = project
  .in(file("example/application"))
  .settings(
    libraryDependencies += "org.scala-lang" %% "toolkit" % "0.1.7"
  )

lazy val exampleAdapterLocal = project
  .in(file("example/adapter/local"))
  .settings(
    libraryDependencies += "org.scala-lang" %% "toolkit" % "0.1.7"
  )
  .dependsOn(exampleApplication)

lazy val exampleWeb = project
  .in(file("example/web"))
  .settings(
    libraryDependencies += "org.scala-lang" %% "toolkit" % "0.1.7",
    libraryDependencies += "dev.zio"  %% "zio-http" % "3.0.1",
    libraryDependencies += "dev.zio" %% "zio-test"         % "2.1.9"  % Test,
    libraryDependencies += "dev.zio" %% "zio-test-sbt"     % "2.1.9"  % Test,
    libraryDependencies += "dev.zio" %% "zio-http-testkit" % "3.0.1" % Test,
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
  .dependsOn(exampleApplication, exampleAdapterLocal)