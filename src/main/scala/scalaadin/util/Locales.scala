package scalaadin.util

import java.util.Locale

object Locales {
  private val CountryVariant = """ (_[A-Z]{2})(_\w+)? """.trim
  val LocaleRegex = ("([a-z]{2})("+CountryVariant+")?").r

  def parse(localeAsString:String):Option[Locale] = localeAsString match {
    case LocaleRegex(lang, countryAndVariant, country, variant) =>
      Some(new Locale(lang, emptyIfNull(country), emptyIfNull(variant)))
    case _ =>
      None
  }

  /**
   * remove trailing '_' or returns '' if the value is null
   */
  private def emptyIfNull(value:String) = if(value==null) "" else value.substring(1)
}