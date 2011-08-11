package scalaadin.domain

trait Context {

  def getChannel(name:String): Channel[AnyRef]

  def evaluate(expression: String): AnyRef

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