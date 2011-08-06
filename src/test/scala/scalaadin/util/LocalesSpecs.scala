package scalaadin.util

import org.specs.Specification
import java.util.Locale

class LocalesSpecs extends Specification {

  "Locales" should {
    "parse valid locales with language only" in {
      Locales.parse("fr") must_== Some(Locale.FRENCH)
      Locales.parse("en") must_== Some(Locale.ENGLISH)
    }
    "parse valid locales with language and country" in {
      Locales.parse("fr_FR") must_== Some(Locale.FRANCE)
      Locales.parse("en_GB") must_== Some(Locale.UK)
    }
    "parse valid locales with variant" in {
      Locales.parse("fr_FR_brEIzh") must_== Some(new Locale("fr","FR","brEIzh"))
      Locales.parse("fr_FR_brEIzh4_ever") must_== Some(new Locale("fr","FR","brEIzh4_ever"))
    }
    "reject invalid locales" in {
      Locales.parse("_") must_== None
      Locales.parse("f") must_== None
      Locales.parse("FR") must_== None
      Locales.parse("f4") must_== None
      Locales.parse("fr_") must_== None
      Locales.parse("fr_f") must_== None
      Locales.parse("fr_fr") must_== None
      Locales.parse("fr_F4") must_== None
      Locales.parse("fr_FR_") must_== None
    }
  }
}