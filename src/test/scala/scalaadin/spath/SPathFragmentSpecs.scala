package scalaadin.spath

import org.specs.Specification
import scalaadin.Samples._
import scalaadin._

class SPathFragmentSpecs extends Specification {

  "SPathFragment" should {
    "handle simple method call" in {
      val address = new Address(17, "rue de Galactica")
      val fragment = SPathFragment("format")
      fragment.select(address) must_== Some("17 rue de Galactica")
    }
    "handle scala `attribute` on class" in {
      val address = new Address(17, "rue de Galactica")
      val fragNum = SPathFragment("num")
      fragNum.select(address) must_== Some(17)
      val fragStreet = SPathFragment("street")
      fragStreet.select(address) must_== Some("rue de Galactica")
    }
    "handle scala `attribute` on case class" in {
      val phone = PhoneNumber("001-002003-004")
      val fragment = SPathFragment("num")
      fragment.select(phone) must_== Some("001-002003-004")
    }
    "handle map access through string key" in {
      val person = newPerson
      val fragment = SPathFragment("attributes", List(StrLiteral("Occupation")))
      fragment.select(person) must_== Some("Consulting detective")
    }
    "handle list access through indice" in {
      val person = newPerson
      val fragment = SPathFragment("phones", List(IntLiteral(1)))
      fragment.select(person) must_== Some(PhoneNumber("0100-0002"))
    }
    "handle array access through indice" in {
      val person = newPerson
      val fragment = SPathFragment("tags", List(IntLiteral(2)))
      fragment.select(person) must_== Some("watson")
    }
    "handle property using java bean convention" in {
      val person = newPerson
      val fragment = SPathFragment("address")
      val addressOpt = fragment.select(person)
      addressOpt.isDefined must_== true
      val address = addressOpt.get.asInstanceOf[Address]
      address.num must_== 221
      address.street must_== "B Baker Street"
    }
  }
}