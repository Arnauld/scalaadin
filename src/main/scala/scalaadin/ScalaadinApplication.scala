package scalaadin

import com.vaadin.Application
import com.vaadin.ui._
import themes.Runo
import web.LoginScreen
import java.util.{Locale, Random, Date}
import org.slf4j.LoggerFactory
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc._

class ScalaadinApplication extends Application {

  val logger = LoggerFactory.getLogger(classOf[ScalaadinApplication])

  var previousScreen: Option[ComponentContainer] = None

  override def init() {
    setTheme(Runo.themeName())
    setLocale(Locale.FRANCE)
    setMainWindow(new Window("Scalaadin Application"))
    loginScreen()
  }

  def loginScreen() {
    getMainWindow.setContent(new LoginScreen(this))
  }

  def login(username: String, password: String) {
    // Collect the subject's principals and credentials
    val token = new UsernamePasswordToken(username, password)

    //”Remember Me” built-in, just do this:
    //token.setRememberMe(true)
    token.setRememberMe(false)

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
      //val user = userDAO.
      showNotification(MessageKeys.Login.LoggedInNotification, "Sherlock!")

    } catch {
      case uae: UnknownAccountException =>
        logger.info("Login failed - Unknown account {}", username)
        loginFailed()
      case ice: IncorrectCredentialsException =>
        logger.info("Login failed - Incorrect credentials for account {}", username)
        loginFailed()
      case lae: LockedAccountException =>
        logger.info("Login failed - Account {} locked", username)
        loginFailed()
      case eae: ExcessiveAttemptsException =>
        logger.info("Login failed - Excessive attempts for account {}", username)
        loginFailed()
      case ae: AuthenticationException =>
        logger.info("Login failed - unexpected error for account {}", username)
        loginFailed()
      //unexpected error?
    }
  }

  def sample() {
    val label = new Label("Hello World")

    val rand = new Random(17L)
    val values = (for (i <- 1 to 25) yield rand.nextInt(100)).mkString(",")

    val s = new org.vaadin.sparklines.Sparklines("Stuff", 0, 0, 0, 100)
    s.setDescription("Everything turned on")
    s.setValue(values)
    s.setAverageVisible(true)
    s.setNormalRangeColor("#444")
    s.setNormalRangeMax(20)
    s.setNormalRangeMin(25)
    s.setNormalRangeVisible(true)
    s.setMaxColor("#f69")
    s.setMinColor("#6f9")

    val mainWindow = getMainWindow
    mainWindow.addComponent(s);
    mainWindow.addComponent(label)
    mainWindow.addComponent(
      new Button("What is the time?",
        new Button.ClickListener() {
          def buttonClick(event: Button#ClickEvent) {
            mainWindow.showNotification("The time is " + new Date())
          }
        }))
  }

  override def setLocale(locale: Locale) {
    super.setLocale(locale)
    showNotification(MessageKeys.i18n.LocaleChangedTo, getLocale)
  }

  def showNotification(key: String, args: AnyRef*) {
    val mainWindow = getMainWindow
    if (mainWindow != null)
      mainWindow.showNotification(message(key, args: _*))
  }

  def message(key: String, args: AnyRef*): String = I18N.message(key, getLocale, args: _*)

}