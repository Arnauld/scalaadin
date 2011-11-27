name := "scalaadin"

organization := "org.technbolts"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.9.1"

scalacOptions ++= Seq("-unchecked", "-deprecation")

seq((webSettings ++ vaadinSettings ++ Seq(
  port := 8081,
  vaadinWidgetSet := "scalaadin.gwt.CombinedWidgetset"
)) :_*)

retrieveManaged := true // remove this once plugins are working or i understand their layout

publishMavenStyle := true

publishTo := Some(Resolver.file("Local", Path.userHome / "Projects" / "arnauld.github.com" / "maven2" asFile)(Patterns(true, Resolver.mavenStyleBasePattern)))


resourceGenerators in Compile <+= resourceManaged in Compile map { dir =>
  val file = dir / "build.properties"
  IO.write(file, """buildAt="""+(new java.util.Date()))
  Seq(file)
}

javaOptions in (run) += "-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

libraryDependencies ++= Seq(
  // logs
  "org.slf4j" % "slf4j-api" % "1.6.0",
  "org.slf4j" % "log4j-over-slf4j" % "1.6.0",
  "org.slf4j" % "jcl-over-slf4j" % "1.6.0",
  "ch.qos.logback" % "logback-classic" % "0.9.25",
  // jetty
  "org.eclipse.jetty" % "jetty-server" % "7.4.2.v20110526" % "container;provided",
  "org.eclipse.jetty" % "jetty-webapp" % "7.4.2.v20110526" % "container;provided",
  "org.eclipse.jetty" % "jetty-servlet" % "7.4.2.v20110526" % "container;provided",
  "javax.servlet" % "servlet-api" % "2.5" % "provided->default",
  // misc
  "joda-time" % "joda-time" % "1.6.2",
  //test
  "org.scala-tools.testing" %% "specs" % "1.6.9" % "test",
  // vaadin + addons
  "com.vaadin" % "vaadin" % "6.6.3",
  "org.vaadin.addons" % "sparklines" % "0.5",
  "org.vaadin.addons" % "gwt-graphics" % "0.9.7",
  // apache shiro - security
  "org.apache.shiro" % "shiro-core" % "1.1.0",
  "org.apache.shiro" % "shiro-web" % "1.1.0"
)

resolvers ++= Seq(
  "Sonatype OSS" at "http://oss.sonatype.org/content/repositories/releases/",
  "Web plugin repo" at "http://siasia.github.com/maven2",
  "Arnauld" at "https://github.com/Arnauld/arnauld.github.com/raw/master/maven2",
  "java net" at "http://download.java.net/maven/2/",
  "vaadin-addons" at "http://maven.vaadin.com/vaadin-addons"
)