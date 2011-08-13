package scalaadin.spath

import util.parsing.combinator._

object SPathParser extends RegexParsers {

  def apply(input: String):ParseResult[SPath] = {
    parseAll(path, input)
  }

  def stringLiteral: Parser[StrLiteral] = ("\"" + """([^"\p{Cntrl}\\]|\\[\\/bfnrt]|\\u[a-fA-F0-9]{4})*""" + "\"").r ^^ {
    case x => StrLiteral(x.substring(1, x.length() - 1))
  }

  def identifier: Parser[Identifier] = """[a-zA-Z_][a-zA-Z_0-9\-:]*""".r ^^ {
    case x => Identifier(x)
  }

  def index: Parser[IntLiteral] = """\d+""".r ^^ {
    case x => IntLiteral(x.toInt)
  }

  def pathFragment: Parser[SPathFragment] = identifier ~ rep("[" ~> (stringLiteral | index) <~ "]") ^^ {
    case k ~ y  =>
      if(y.isEmpty)
        AccessorRaw(k.value)
      else
        AccessorWithIndices(k.value, y)
  }

  def path: Parser[SPath] = pathFragment ~ rep("." ~> pathFragment) ^^ {
    case fragment ~ fragments => SPath(fragment :: fragments)
  }

}