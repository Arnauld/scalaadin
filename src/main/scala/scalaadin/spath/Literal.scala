package scalaadin.spath

sealed abstract class Literal
object Literal {
  def apply(value: Int):IntLiteral = IntLiteral(value)
  def apply(value: String):StrLiteral = StrLiteral(value)
}

case class IntLiteral(value: Int) extends Literal

case class StrLiteral(value: String) extends Literal
