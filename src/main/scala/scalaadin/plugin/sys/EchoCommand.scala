package scalaadin.plugin.sys

import scalaadin.domain.{SimpleCommand, CommandContext}

class EchoCommand extends SimpleCommand("echo ") {

  def description = "write arguments to the standard output"

  override def executeArguments(context: CommandContext, arguments: String) {
    val trimmed = arguments.trim()
    context.dataOut(trimmed)
    context.logInfo(trimmed)
  }

}