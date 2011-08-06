package scalaadin.web

import scalaadin.{MessageKeys, ScalaadinApplication}
import com.vaadin.ui.{Button, HorizontalLayout}
import java.util.Locale
import com.vaadin.ui.themes.BaseTheme
class LanguageBar(app: ScalaadinApplication) extends HorizontalLayout {
  private var buttons:List[Button] = Nil

  setHeight("50px")
  setMargin(false,true,false,true)
  setSpacing(true)

  val guiLocales = Array(Locale.US, Locale.FRANCE)
  guiLocales.foreach({
    locale =>
      val selector = new Button(app.message(MessageKeys.i18n.labelFor(locale)), new Button.ClickListener() {
        def buttonClick(event: Button#ClickEvent) {
          app.setLocale(locale)
          updateButtons()
        }
      })
      selector.setData(locale)
      selector.setStyleName(BaseTheme.BUTTON_LINK)
      //selector.setIcon(new ThemeResource("img/" + locale.getLanguage + "_" + locale.getCountry + ".png"))
      addComponent(selector)
      buttons = selector :: buttons
  })

  // enable or not according to current locale
  updateButtons()

  private def updateButtons() {
    val appLocale = app.getLocale
    buttons.foreach({ b =>
      val butLocale = b.getData.asInstanceOf[Locale]
      b.setEnabled(!appLocale.equals(butLocale))
    })
  }

}
