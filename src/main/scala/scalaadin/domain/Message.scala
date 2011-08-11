package scalaadin.domain

case class Message[T](payload:T, headers:Map[String,String] = Map.empty[String,String]) {
}




