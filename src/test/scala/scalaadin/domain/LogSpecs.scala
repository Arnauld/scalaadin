package scalaadin.domain

import org.specs.Specification

class LogSpecs extends Specification {

  val text = "yeahh!"

  "Log" should {
    "keep constant ordinals" in {
      Log.Error.ordinal must_== 10
      Log.Warn .ordinal must_== 8
      Log.Info .ordinal must_== 6
      Log.Debug.ordinal must_== 4
      Log.Trace.ordinal must_== 2
    }
    "create trace message" in {
      val log = Log.Trace(text)
      log.level must_== Log.Trace
      log.content must_== text
      log.throwable must_== None
    }
    "create debug message" in {
      val log = Log.Debug(text)
      log.level must_== Log.Debug
      log.content must_== text
      log.throwable must_== None
    }
    "create info message" in {
      val log = Log.Info(text)
      log.level must_== Log.Info
      log.content must_== text
      log.throwable must_== None
    }
    "create warn message" in {
      val log = Log.Warn(text)
      log.level must_== Log.Warn
      log.content must_== text
      log.throwable must_== None
    }
    "create error message" in {
      val log = Log.Error(text)
      log.level must_== Log.Error
      log.content must_== text
      log.throwable must_== None
    }

    "support equals with same content" in {
      val log1 = Log(Log.Error, "bob")
      val log2 = Log(Log.Error, "bob")
      log1 must_== log2
    }
  }
}