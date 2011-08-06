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

[Authentication: Vaadin + Shiro](https://github.com/Arnauld/scalaadin/wiki/Authentication:-Vaadin+Shiro)

Libraries
=========

* [SBT 0.10.0](https://github.com/harrah/xsbt)
* [SBT web plugin](https://github.com/siasia/xsbt-web-plugin)
* [SBT vaadin plugin](https://github.com/Arnauld/sbt-vaadin-plugin)
* [Vaadin](http://vaadin.com/home)
* [Shiro](http://shiro.apache.org/)


Later

* [Cassandra](http://cassandra.apache.org/)

