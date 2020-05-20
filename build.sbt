import Dependencies._
import sbt.Keys._

def testScope(project: ProjectReference) = project % "test->test;test->compile"

val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "com.github.merlijn",
  scalaVersion := "2.13.2",
  crossScalaVersions := Seq("2.13.2"),
  fork := true,
  testOptions += Tests.Argument(TestFrameworks.JUnit, "-v"),
  javacOptions := Seq("-source", jvmV, "-target", jvmV),
  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
//    "-Ywarn-dead-code", // disabled because it flag js.native as dead code: https://github.com/scala/bug/issues/11942
    "-language:higherKinds",
    "-language:existentials",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-encoding", "utf8",
    s"-target:jvm-$jvmV",
    "-Xfatal-warnings"
  ),
  packageOptions in (Compile, packageBin) +=
    Package.ManifestAttributes(
      "Build-Time" -> new java.util.Date().toString,
      "Build-Commit" -> git.gitHeadCommit.value.getOrElse("No Git Revision Found")
    )
)

val dependencyOverrideSettings = Seq(
  // note that this does NOT add the dependencies, just forces the version
  dependencyOverrides ++= Seq(
    //
  )
)

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

lazy val defaultModuleSettings = commonSettings ++ dependencyOverrideSettings ++ Revolver.settings ++ SonatypePublish.settings

lazy val pixelFPG =
  project
  .in(file("."))
  .settings(defaultModuleSettings)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
    moduleName := "pixel-fpg",
    fork := false,
    libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "1.0.0",
        "com.lihaoyi"  %%% "scalatags"   % "0.9.1",
        "com.lihaoyi"  %%% "scalarx"     % "0.4.2"
      ) ++
        testDeps(
          scalaTest,
          slf4jApi,
          logback
        )
  ).enablePlugins(ScalaJSPlugin)
