import sbt._

//noinspection TypeAnnotation
object Dependencies {

  val jvmV = "1.8"

  // Last stable release
  val breeze = "org.scalanlp" %% "breeze" % "1.0"

  val breezeNatives = "org.scalanlp" %% "breeze-natives" % "1.0"

  val breezeViz = "org.scalanlp" %% "breeze-viz" % "1.0"

  val scalaFx = "org.scalafx" %% "scalafx" % "14-R19"

  lazy val javaFXModules =
    Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
      .map( m => "org.openjfx" % s"javafx-$m" % "14.0.1" classifier "mac")

  val typeSafeConfig =            "com.typesafe"               %  "config"                             % "1.3.1"

  val scalaTest =                 "org.scalatest"              %% "scalatest"                          % "3.0.5"

  val levelDB   =                 "org.iq80.leveldb"           %  "leveldb"                            % "0.7"
  val levelDBJni =                "org.fusesource.leveldbjni"  %  "leveldbjni-all"                     % "1.8"

  val logback =                   "ch.qos.logback"             %  "logback-classic"                    % "1.2.2"
  val ficusConfig =               "com.iheart"                 %% "ficus"                              % "1.4.0"

  val scalaGraph  =               "org.scala-graph"            %% "graph-core"                         % "1.11.5"
  val scalaGraphDot =             "org.scala-graph"            %% "graph-dot"                          % "1.11.5"
  val graphvizJava =              "guru.nidi"                  %  "graphviz-java"                      % "0.8.0"

  val catsEffect =                "org.typelevel"              %% "cats-effect"                        % "1.2.0"
  val catsCore =                  "org.typelevel"              %% "cats-core"                          % "1.5.0"

  def scalaReflect(scalaV: String): ModuleID = "org.scala-lang"%  "scala-reflect"                      % scalaV
  val javaxInject =               "javax.inject"               %  "javax.inject"                       % "1"
  val reflections =               "org.reflections"            %  "reflections"                        % "0.9.11"
  val liftJson =                  "net.liftweb"                %% "lift-json"                          % "3.3.0"
  
  val guava =                     "com.google.guava"           %  "guava"                              % "19.0"
  val findbugs =                  "com.google.code.findbugs"   %  "jsr305"                             % "1.3.9"

  val slf4jApi =                  "org.slf4j"                  %  "slf4j-api"                          % "1.7.25"
  val scalaCheck =                "org.scalacheck"             %% "scalacheck"                         % "1.13.4"

  def scopeDeps(scope: String, modules: Seq[ModuleID]) =  modules.map(m => m % scope)
  def compileDeps(modules: ModuleID*) = modules.toSeq
  def testDeps(modules: ModuleID*) = scopeDeps("test", modules)

  def providedDeps(modules: ModuleID*) = scopeDeps("provided", modules)
}
