package scalaadin.domain

import org.slf4j.LoggerFactory

trait Endpoint[T] {
  def name:String
  def onMessage(message:Message[T])
}

object Endpoint {

  def apply[T,R](_name:String, _transform:(Message[T])=>Message[R], _next:Endpoint[R]):Endpoint[T] =
    new TransformEndpoint[T,R] {
      def name = _name
      def next = _next
      def transform(value: Message[T]) =  _transform(value)
    }

  def apply[T](_name:String, callback:(Message[T])=>Any):Endpoint[T] =
    new Endpoint[T] {
      def name = _name
      def onMessage(value: Message[T]) =  callback(value)
    }
}



trait TransformEndpoint[T,R] extends Endpoint[T] {
  def next:Endpoint[R]
  def transform(value: Message[T]): Message[R]

  def onMessage(message: Message[T]) {
    next.onMessage(transform(message))
  }
}

trait MemoryEndpoint[T] extends Endpoint[T] {

  lazy val logger = LoggerFactory.getLogger(classOf[MemoryEndpoint[T]])

  var messages:List[Message[T]] = Nil

  def onMessage(message: Message[T]) {
    val count = synchronized {
      messages = message :: messages
      messages.length
    }
    logger.debug("[" + name + "] Message received (got " + count + " message(s)): " + message)
  }
}
