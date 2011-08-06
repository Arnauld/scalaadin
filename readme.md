DEV Usage
=========

    sbt> vaadin-generate-widgetset
    ...
    sbt> vaadin-compile-widgetset
    ...
    sbt> jetty-run


SBT Notes
=========

    set logLevel := sbt.Level.Debug

Vaadin Notes
============

see notes in [SBT vaadin plugin](https://github.com/Arnauld/sbt-vaadin-plugin) and add the declared `widgetset` in `web.xml`

```xml
    <servlet>
        <servlet-name>ScalaadinApplication</servlet-name>
        <servlet-class>com.vaadin.terminal.gwt.server.ApplicationServlet</servlet-class>
        <init-param>
            <description>Vaadin application class to start</description>
            <param-name>application</param-name>
            <param-value>scalaadin.ScalaadinApplication</param-value>
        </init-param>
        <init-param>
            <param-name>widgetset</param-name>
            <param-value>scalaadin.gwt.CombinedWidgetset</param-value>
        </init-param>
    </servlet>
```

see also

* [Robert Lally - Vaadin book code in scala](http://www.robertlally.com/blog/category/vaadin)

Shiro Notes and integration with Vaadin
=======================================

Apache Shiro is a powerful and easy-to-use Java security framework that performs authentication, authorization,
cryptography, and session management.

* [Xebia 2011/04 - FR](http://blog.xebia.fr/2011/04/18/apache-shiro/)
* [Xebia 2011/04 - EN](http://blog.xebia.com/2011/04/apache-shiro/)
* [Shiro web](http://shiro.apache.org/web.html#Web-Custom%257B%257BWebEnvironment%257D%257DClass)
* [Authentication in Vaadin Application](http://vaadin.com/wiki/-/wiki/Main/Authenticating%20Vaadin-based%20applications?p_r_p_185834411_title=Authenticating%20Vaadin-based%20applications)

To "secure" the vaadin application, Shiro is plugged with a custom Realm (`ScalaadinRealm`).

in `src/main/resources/shiro.ini`

    [main]
    authcBasicRealm = scalaadin.security.shiro.ScalaadinRealm

    [urls]
    /admin/** = authcBasic
    /** = anon


User is then explicitly authenticated within the Vaadin application, instead of using basic auth. or form auth.
If the user is successfully authenticated `scalaadin.ScalaadinApplication#login`, then the Vaadin application
load the secured resources, i.e. it loads the application window in place of the login screen.

in `scalaadin.ScalaadinApplication#login`

```scala
    def login(username: String, password: String) {
        // Collect the subject's principals and credentials
        val token = new UsernamePasswordToken(username, password)

        //With most of Shiro, you'll always want to make sure you're working
        // with the currently executing user, referred to as the subject
        val currentUser = SecurityUtils.getSubject

        def loginFailed() {
          showNotification(MessageKeys.Login.LoginFailedNotification, username)
        }

        try {
          //Authenticate the subject by passing the user name and password token
          // into the login method. One delegates login to Shiro, according to its
          // conf/realms the password will be checked and roles/permissions will
          // be assigned
          currentUser.login(token)

          // still there, auth is successful, let's retrieve the corresponding
          // user from the application layer
          userRepository.findByLogin(username) match {
            case Some(user) =>
                showNotification(MessageKeys.Login.LoggedInNotification, user.screenName)
                setUser(user)
                loadProtectedResources()
            case None =>
                logger.error("Account {} has been authentified but is not known...")
                throw new UnknownAccountException
          }

        } catch {
          case uae: UnknownAccountException =>
            logger.info("Login failed - Unknown account {}", username)
            loginFailed()
          case ice: IncorrectCredentialsException =>
            ...
          case lae: LockedAccountException =>
            ...
          case eae: ExcessiveAttemptsException =>
            ...
          case ae: AuthenticationException =>
            ...
        }
     }
``

By doing so, *the login form can be written in Vaadin directly* while still using the security framework.

Libraries
=========

* [SBT 0.10.0](https://github.com/harrah/xsbt)
* [SBT web plugin](https://github.com/siasia/xsbt-web-plugin)
* [SBT vaadin plugin](https://github.com/Arnauld/sbt-vaadin-plugin)
* [Vaadin](http://vaadin.com/home)
* [Shiro](http://shiro.apache.org/)


Later

* [Cassandra](http://cassandra.apache.org/)

