import mill._, scalalib._

object server extends ScalaModule with NativeImageModule {
  def scalaVersion = "2.12.8"
  def ivyDeps = Dependencies.ivy
}

trait NativeImageModule extends ScalaModule {
  private def graalVmHome = T.input {
    T.ctx().env.getOrElse("GRAALVM_HOME", sys error "GRAALVM_HOME env variable undefined")
  }

  def nativeImage = T {
    import ammonite.ops._
    implicit val workingDirectory = T.ctx().dest
    val assemblyPath = assembly().path
    val graalHome = graalVmHome()
    val command = s"$graalHome/bin/native-image"
    val commandResult =
      %%(command,
        "--class-path", assemblyPath.toString,
        "-H:+ReportUnsupportedElementsAtRuntime",
        "-R:+PrintGC",
        finalMainClass()
      )
    finalMainClass()
  }
}

object Dependencies {
  val Http4sVersion = "0.20.0-M5"
  val LogbackVersion = "1.2.3"

  def ivy = Agg(
    ivy"org.http4s::http4s-blaze-server:$Http4sVersion",
    ivy"org.http4s::http4s-circe:$Http4sVersion",
    ivy"org.http4s::http4s-dsl:$Http4sVersion",
    ivy"ch.qos.logback:logback-classic:$LogbackVersion"
  )
}
