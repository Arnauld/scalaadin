// remove this once plugins are working
retrieveManaged := true // and the dependencies will be copied to lib_managed as a build-local cache

resolvers += "Web plugin repo" at "http://siasia.github.com/maven2"

libraryDependencies <++= sbtVersion({ sbt_version:String => 
	Seq("com.github.siasia" %% "xsbt-web-plugin" % sbt_version,
	    "org.technbolts" %% "sbt-vaadin-plugin" %  "0.0.1-SNAPSHOT")})