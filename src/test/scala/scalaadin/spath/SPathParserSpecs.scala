package scalaadin.spath

import org.specs.Specification

class SPathParserSpecs extends Specification {
  import SPathParser._

  "spath" should {
    "parse identifier" in {
      val path = SPathParser("plugins").get
      path.parts must_== List(AccessorRaw("plugins"))
    }

    "parse identifier with numerical indices" in {
      val path = SPathParser("plugins[17]").get
      path.parts must_== List(AccessorWithIndices("plugins", List(IntLiteral(17))))
    }

    "parse identifier with literal index" in {
      val path = SPathParser("""plugins["eav"]""").get
      path.parts must_== List(AccessorWithIndices("plugins", List(StrLiteral("eav"))))
    }

    "parse two-sized path with indices" in {
      val path = SPathParser("plugins.list[3]").get
      path.parts must_== List(//
        AccessorRaw("plugins"),//
        AccessorWithIndices("list",List(IntLiteral(3)))//
      )
    }

    "parse complex path with multiple indices" in {
      val path = SPathParser("plugins.list[3].attributes[1][7]").get
      path.parts must_== List(//
        AccessorRaw("plugins"),//
        AccessorWithIndices("list", List(IntLiteral(3))),//
        AccessorWithIndices("attributes", List(IntLiteral(1), IntLiteral(7)))//
      )
    }
  }
}