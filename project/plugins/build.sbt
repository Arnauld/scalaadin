// remove this once plugins are working
retrieveManaged := true // and the dependencies will be copied to lib_managed as a build-local cache

resolvers ++= Seq(
  "Web plugin repo" at "http://siasia.github.com/maven2",
  "Arnauld" at "https://github.com/Arnauld/arnauld.github.com/raw/master/maven2"
)

addSbtPlugin("org.technbolts" % "sbt-vaadin-plugin" %  "0.0.2-SNAPSHOT")

libraryDependencies <++= (sbtVersion) { (sbt_ver) =>
	Seq("com.github.siasia" %% "xsbt-web-plugin" % (sbt_ver+"-0.2.10"))
}
