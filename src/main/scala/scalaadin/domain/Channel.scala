package scalaadin.domain

object Channel {
  val LOG_TRACE = "log-trace"
  val LOG_DEBUG = "log-debug"
  val LOG_INFO  = "log-info"
  val LOG_WARN  = "log-warn"
  val LOG_ERROR = "log-error"
  val DATA      = "data"

  lazy val logger = org.slf4j.LoggerFactory.getLogger(classOf[Channel[_]])

  def apply[T](channelName:String, endpoint:Endpoint[T]):Channel[T] = {
    val channel = new Channel[T] {
      def name = channelName
    }
    channel.endpoints = List(endpoint)
    channel
  }
}

trait Channel[T] extends Endpoint[T] {
  import Channel._
  
  var endpoints:List[Endpoint[T]] = Nil

  def addEndpoint(endpoint:Endpoint[T]):Channel[T] = {
    endpoints = endpoint :: endpoints
    this
  }

  def publishPayload(payload:T) {
    publish(Message(payload))
  }

  def publish(message:Message[T]) {
    logger.debug("Publishing message to " + endpoints.length + " endpoint(s)")
    endpoints.foreach(_.onMessage(message))
  }

  def onMessage(message:Message[T]) {
    publish(message)
  }
}
