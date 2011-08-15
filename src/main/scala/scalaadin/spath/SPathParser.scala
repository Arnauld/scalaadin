package scalaadin.spath

import util.parsing.combinator._

/**
 * <pre>
 * stringLiteral := '"' [^"]* '"'
 * identifier    := [a-zA-Z_][a-zA-Z_0-9-:]*
 * integer         := [0-9]+
 * pathFragment  := identifier ( '[' (stringLiteral|integer) ']' )*
 * path          := pathFragment ( '.' pathFragment)*
 * </pre>
 *
 * e.g.
 * <pre>
 *   plugins.list[1].attributes["firstName"]
 * </pre>
 *
 */
object SPathParser extends RegexParsers {

  def apply(input: String):ParseResult[SPath] = {
    parseAll(path, input)
  }

  def stringLiteral: Parser[StrLiteral] = ("\"" + """([^"\p{Cntrl}\\]|\\[\\/bfnrt]|\\u[a-fA-F0-9]{4})*""" + "\"").r ^^ {
    case x =>
      // unquote
      StrLiteral(x.substring(1, x.length() - 1))
  }

  def identifier: Parser[Identifier] = """[a-zA-Z_][a-zA-Z_0-9\-:]*""".r ^^ {
    case x => Identifier(x)
  }

  def integer: Parser[IntLiteral] = """\d+""".r ^^ {
    case x => IntLiteral(x.toInt)
  }

  def pathFragment: Parser[SPathFragment] = identifier ~ rep("[" ~> (stringLiteral | integer) <~ "]") ^^ {
    case k ~ y  =>
      if(y.isEmpty)
        SPathFragment(k.value)
      else
        SPathFragment(k.value, y)
  }

  def path: Parser[SPath] = pathFragment ~ rep("." ~> pathFragment) ^^ {
    case fragment ~ fragments => SPath(fragment :: fragments)
  }

}