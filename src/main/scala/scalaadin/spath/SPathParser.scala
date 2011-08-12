package scalaadin.spath

import util.parsing.combinator._

object SPathParser extends RegexParsers {

  def apply(input: String) = {
    parseAll(path, input)
  }

  sealed abstract class Literal

  case class IntLiteral(value: Int) extends Literal

  case class StrLiteral(value: String) extends Literal

  case class Identifier(value: String)

  trait PathFragment {
    def identifier:String
    def indices: List[Literal] = Nil
  }

  case class AccessorRaw(identifier: String) extends PathFragment
  case class AccessorWithIndices(identifier: String, override val indices: List[Literal]) extends PathFragment

  case class Path(parts: List[PathFragment])

  def stringLiteral: Parser[StrLiteral] = ("\"" + """([^"\p{Cntrl}\\]|\\[\\/bfnrt]|\\u[a-fA-F0-9]{4})*""" + "\"").r ^^ {
    case x => StrLiteral(x.substring(1, x.length() - 1))
  }

  def identifier: Parser[Identifier] = """[a-zA-Z_][a-zA-Z_0-9\-:]*""".r ^^ {
    case x => Identifier(x)
  }

  def index: Parser[IntLiteral] = """\d+""".r ^^ {
    case x => IntLiteral(x.toInt)
  }

  def pathFragment: Parser[PathFragment] = identifier ~ rep("[" ~> (stringLiteral | index) <~ "]") ^^ {
    case k ~ y  =>
      if(y.isEmpty)
        AccessorRaw(k.value)
      else
        AccessorWithIndices(k.value, y)
  }

  def path: Parser[Path] = pathFragment ~ rep("." ~> pathFragment) ^^ {
    case r ~ o => Path(r :: o)
  }

}