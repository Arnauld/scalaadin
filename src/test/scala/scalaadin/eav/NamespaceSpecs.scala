package scalaadin.eav

import org.specs.Specification

class NamespaceSpecs extends Specification {

  "Root Namespace" should {
    "have constants default values" in {
      Namespace.Root.name must_== "/"
      Namespace.Root.qualifiedName must_== "/"
      Namespace.Root.hasParent must_== false
    }
  }

  "Namespace" should {
    "returns valid qualified names" in {
      val ns = Namespace("sys")
      ns.name must_== "sys"
      ns.qualifiedName must_== "/sys"
      ns.hasParent must_== true
      ns.parent must_== Some(Namespace.Root)
    }
  }
}