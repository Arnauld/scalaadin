// remove this once plugins are working
retrieveManaged := true // and the dependencies will be copied to lib_managed as a build-local cache

resolvers += "Web plugin repo" at "http://siasia.github.com/maven2"

libraryDependencies <+= sbtVersion("com.github.siasia" %% "xsbt-web-plugin" % _)