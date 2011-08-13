package scalaadin.domain

trait ExpressionResolver {
  def resolve(expression: String): AnyRef
}

trait Context {

  def get(expression: String): Option[AnyRef]

  def getChannel(name:String): Channel[AnyRef]

  def log(channelName:String, message:String) {
    getChannel(channelName).publishPayload(message)
  }
  
  def dataOut(value:AnyRef) {
    getChannel(Channel.DATA).publishPayload(value)
  }

  def logTrace(message: String) {
    log(Channel.LOG_TRACE, message)
  }

  def logDebug(message: String) {
    log(Channel.LOG_DEBUG, message)
  }

  def logInfo(message: String) {
    log(Channel.LOG_INFO, message)
  }

  def logWarn(message: String) {
    log(Channel.LOG_WARN, message)
  }

  def logError(message: String) {
    log(Channel.LOG_ERROR, message)
  }

}

trait CommandContext extends Context {
}