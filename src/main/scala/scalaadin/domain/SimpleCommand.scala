package scalaadin.domain

abstract class SimpleCommand(val prefix:String) extends Command {

  def analyse(context: CommandContext, input: String) = {
    if(input.startsWith(prefix))
      Command.Accepted
    else if(prefix.startsWith(input))
      Command.Incomplete(List(prefix))
    else
      Command.Rejected
  }

  def execute(context: CommandContext, input: String) {
    if(input.startsWith(prefix)) {
      val arguments = input.substring(prefix.length())
      executeArguments(context, arguments)
    }
    else
      throw new IllegalArgumentException("Incompatible command does not start with <" + prefix + "> got: " + input)
  }

  protected def executeArguments(context: CommandContext, arguments: String)
}