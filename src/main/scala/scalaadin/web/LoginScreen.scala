package scalaadin.web

import org.slf4j.LoggerFactory
import scalaadin.{MessageKeys, ScalaadinApplication}
import com.vaadin.ui._
import com.vaadin.ui.Button.ClickListener
import com.vaadin.data.Validator.InvalidValueException

class LoginScreen(app: ScalaadinApplication) extends VerticalLayout {
  val logger = LoggerFactory.getLogger(classOf[LoginScreen])

  setSizeFull()

  // Language bar in the top-right corner for selecting
  // user interface language
  val languageBar = new LanguageBar(app)
  addComponent(languageBar)
  setComponentAlignment(languageBar, Alignment.TOP_RIGHT)

  val form = new Form()
  form.setDescription("Please specify name of the person and the city where the person lives in.")
  form.addField("username", new TextField(app.message(MessageKeys.Login.Username)))
  form.addField("password", new PasswordField(app.message(MessageKeys.Login.Password)))
  form.getField("username").setRequired(true)
  form.getField("password").setRequired(true)
  form.setFooter(new VerticalLayout)
  form.getFooter.addComponent(
        new Label("This is the footer area of the Form. "+
                  "You can use any layout here. "+
                  "This is nice for buttons."))

  // Have a button bar in the footer.
  val loginButton = new Button(app.message(MessageKeys.Login.Action), new ClickListener {
    def buttonClick(event: Button#ClickEvent) {
      try{
        // validate the form
        form.commit()

        // ok so, let's check
        app.login(
          form.getField("username").getValue.asInstanceOf[String],
          form.getField("password").getValue.asInstanceOf[String])
      }
      catch {
        case e:InvalidValueException =>
          logger.debug("Failed to validate login form: invalid parameters", e)
        case e =>
          logger.error("Failed to validate login form: unexpected error", e)
      }
    }
  })
  val actionBar = new HorizontalLayout
  actionBar.setHeight("25px")
  actionBar.addComponent(loginButton)
  actionBar.setComponentAlignment(loginButton, Alignment.TOP_RIGHT)
  form.getFooter.addComponent(actionBar)

  val loginPanel = new Panel(app.message(MessageKeys.Login.Title))
  loginPanel.setWidth("400px")
  loginPanel.addComponent(form)
  addComponent(loginPanel);
  setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);

  addComponent(new FooterBar(app));
}
