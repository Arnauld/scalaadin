package scalaadin.plugin.sys

import org.specs.Specification
import scalaadin.domain.{Log, Message, Command, CommandContextMock}

class EchoCommandSpecs extends Specification {

  def newCommandContext: CommandContextMock = new CommandContextMock {}

  "Echo command" should {
    "support completion based on the starts of 'echo'" in {
      val context = newCommandContext
      val echoCommand = new EchoCommand
      echoCommand.analyse(context, "e") must_== Command.Incomplete(List("echo "))
      echoCommand.analyse(context, "ec") must_== Command.Incomplete(List("echo "))
      echoCommand.analyse(context, "ech") must_== Command.Incomplete(List("echo "))
      echoCommand.analyse(context, "echo") must_== Command.Incomplete(List("echo "))
    }
    "write its parameters in the Context's log and data channels" in {
      val context = newCommandContext
      val echoCommand = new EchoCommand
      echoCommand.execute(context, "echo Hello Sherlock!")
      context.logEndpoint.messages must_== List(Message(Log.Info("Hello Sherlock!")))
      context.dataEndpoint.messages must_== List(Message("Hello Sherlock!"))
    }
    "reject invalid input during analysis" in {
      val context = newCommandContext
      val echoCommand = new EchoCommand
      echoCommand.analyse(context, "hey") must_== Command.Rejected
    }
    "reject invalid input during execution" in {
      val context = newCommandContext
      val echoCommand = new EchoCommand
      echoCommand.execute(context, "hey") must throwAn[IllegalArgumentException]
    }
  }
}