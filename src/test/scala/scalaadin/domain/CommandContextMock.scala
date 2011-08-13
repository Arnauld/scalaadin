package scalaadin.domain

import java.lang.IllegalStateException

trait CommandContextMock extends CommandContext {

  var data = Map.empty[String, AnyRef]

  def get(expression: String):  Option[AnyRef] = data.get(expression)

  var logEndpoint = new MemoryEndpoint[Log] {
    def name = "log"
  }

  val dataEndpoint = new MemoryEndpoint[AnyRef] {
    def name = Channel.DATA
  }

  def bindLogChannel(name:String, t:(Message[AnyRef])=>Message[Log]) =
    (name -> Channel(name, Endpoint[AnyRef,Log](name, t(_), logEndpoint)))

  var channels = Map(
    bindLogChannel(Channel.LOG_TRACE, { m => Message(Log.Trace(m.payload.toString))}),
    bindLogChannel(Channel.LOG_DEBUG, { m => Message(Log.Debug(m.payload.toString))}),
    bindLogChannel(Channel.LOG_INFO,  { m => Message(Log.Info (m.payload.toString))}),
    bindLogChannel(Channel.LOG_WARN,  { m => Message(Log.Warn (m.payload.toString))}),
    bindLogChannel(Channel.LOG_ERROR, { m => Message(Log.Error(m.payload.toString))}),
    (Channel.DATA -> Channel(Channel.DATA, dataEndpoint))
  )


  def getChannel(channelName: String) = channels.get(channelName).getOrElse(
    throw new IllegalStateException("Unknown channel named <" + channelName + ">"))

}