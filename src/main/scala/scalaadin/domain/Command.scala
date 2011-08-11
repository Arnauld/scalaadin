package scalaadin.domain

object Command {
  sealed trait Analysis
  case object Rejected extends Analysis
  case object Accepted extends Analysis
  case class  Incomplete(proposals: List[String]) extends Analysis
  case class  InvalidArguments(errors: List[ArgumentError]) extends Analysis
}

case class ArgumentError(description:String)

trait Command {
  def description:String
  def analyse(context:CommandContext, input:String):Command.Analysis
  def execute(context:CommandContext, input:String)
}