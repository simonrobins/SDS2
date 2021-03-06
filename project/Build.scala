import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "SDS2"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "com.yammer.metrics" % "metrics-core" % "2.2.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
