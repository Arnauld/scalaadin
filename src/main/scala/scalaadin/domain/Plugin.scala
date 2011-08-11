package scalaadin.domain

import scalaadin.domain.Command.{Rejected, Accepted}

trait Plugin {

  def pluginName:String

  def pluginDescription:String = pluginName

  def commandList:List[Command] = Nil

  def commandCompletion(context:CommandContext, input:String):List[Command.Analysis] =
    commandList.foldLeft(List.empty[Command.Analysis])( (analyses, command) => 
      command.analyse(context, input) match {
        case Rejected => analyses /* skip */
        case other => other :: analyses
      }
    )

  def commandMatching(context:CommandContext, input:String):List[Command] =
    commandList.foldLeft(List.empty[Command])( (founds, command) =>
      command.analyse(context, input) match {
        case Accepted => command :: founds
        case _ => founds /* skip */
      }
    )

}
