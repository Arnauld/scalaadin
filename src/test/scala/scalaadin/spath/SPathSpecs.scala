package scalaadin.spath

import org.specs.Specification
import scalaadin.Samples._

class SPathSpecs extends Specification {

  "SPath" should {
    "handle two path fragments resolution" in {
      val person = newPerson
      val spath = SPath("address.street")
      spath.select(person) must_== Some("B Baker Street")
    }
    "handle two path fragments with indice resolution" in {
      val person = newPerson
      val spath = SPath("phones[1].num")
      spath.select(person) must_== Some("0100-0002")
    }
  }
}