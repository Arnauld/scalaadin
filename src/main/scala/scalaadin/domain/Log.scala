package scalaadin.domain

object Log {
  sealed abstract class Level(val ordinal:Int)
  case object Error extends Level(10) {
    def apply(message:String) = Log(Error, message)
  }
  case object Warn  extends Level(8) {
    def apply(message:String) = Log(Warn, message)
  }
  case object Info  extends Level(6) {
    def apply(message:String) = Log(Info, message)
  }
  case object Debug extends Level(4) {
    def apply(message:String) = Log(Debug, message)
  }
  case object Trace extends Level(2) {
    def apply(message:String) = Log(Trace, message)
  }
  
  def apply(level:Level, content:String):Log = new Log(level, content, None)
  def apply(level:Level, content:String, throwable:Throwable):Log = new Log(level, content, Some(throwable))
}

sealed class Log(val level:Log.Level, val content:String, val throwable:Option[Throwable] = None) {
  override def equals(obj: Any):Boolean = {
    if(obj!=null && obj.isInstanceOf[Log]) {
      val other = obj.asInstanceOf[Log]
      level == other.level && content == other.content // skip testing on throwable
    }
    else
      false
  }

  override def hashCode() = level.ordinal + 17*content.hashCode()

  override def toString = "Log(" + level + ":" + content + ")"
}
