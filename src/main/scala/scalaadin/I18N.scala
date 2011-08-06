package scalaadin

import java.util.{ResourceBundle, Locale}

object I18N {
  implicit val locale = Locale.FRANCE
  val bundle = ResourceBundle.getBundle("messages")

  def message(key:String, args:AnyRef*):String = message(key, locale, args:_*)

  def message(key:String, locale:Locale, args:AnyRef*):String = raw(key, locale).formatLocal(locale, args:_*)

  import MessageKeys._
  def raw(key:String, locale:Locale):String = key match {
    case ApplicationTitle => "Scalaadin"
    case _ =>
      if(bundle.containsKey(key))
        bundle.getString(key)
      else
        key
  }
}

object MessageKeys {
  val ApplicationTitle = "application.title"

  object i18n {
    val LocaleChangedTo = "i18n.locale.changed-to"
    def labelFor(locale:Locale) = "i18n.locale." + locale.getLanguage + ".label"
  }

  object Login {
    val Title = "login.title"
    val Username = "login.user.label"
    val Password = "login.pass.label"
    val Action = "login.action.label"
    val LoggedInNotification = "login.logged-in.notification"
    val LoginFailedNotification = "login.failed.notification"
  }
}