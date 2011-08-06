package scalaadin.eav

import xml.NodeSeq
import org.joda.time.{LocalTime, LocalDateTime, LocalDate}
import java.util.Locale
import scalaadin.util.Locales

case class Ratio(numerator: Int, denominator: Int = 100) {
  def toPercent: Int = (numerator * 100) / denominator
}

sealed abstract class DataType(valueType:Class[_]) {
  def isPrimitive = true
  def isEntityRef = !isPrimitive
}

case object Int32Type extends DataType(classOf[Int])
case object Int64Type extends DataType(classOf[Long])
case object RatioType extends DataType(classOf[Ratio]) {
  val RatioRegex = """(\-?\d+)/(\d+)""".r
  val NumRegex = """(\-?\d+)""".r

  def parse(value: String): Option[Ratio] = value.trim() match {
    case RatioRegex(numerator, denominator) => Some(Ratio(numerator.toInt, denominator.toInt))
    case NumRegex(numerator) => Some(Ratio(numerator.toInt, 1))
    case _ => None
  }
}
case object TextType  extends DataType(classOf[String])
case object XmlType   extends DataType(classOf[NodeSeq])
case object TimestampType extends DataType(classOf[Long])
case object LocaleType extends DataType(classOf[Locale]) {
  def fromString(value: String): Option[Locale] = Locales.parse(value)
}
case object LocalDateType extends DataType(classOf[LocalDate]) {
  val DateRegex = """([\d]{4})/([\d]{2})/([\d]{2})""".r

  def fromString(value: String): Option[LocalDate] = value.trim() match {
    case DateRegex(year, month, day) => Some(new LocalDate(year.toInt, month.toInt, day.toInt))
    case _ => None
  }
}
case object LocalTimeType extends DataType(classOf[LocalTime]) {
  val TimeRegex = """([\d]{2}):([\d]{2}):([\d]{2})""".r

  def fromString(value: String): Option[LocalTime] = value.trim() match {
    case TimeRegex(hour, minute, second) => Some(new LocalTime(hour.toInt, minute.toInt, second.toInt))
    case _ => None
  }
}
case object LocalDateTimeType extends DataType(classOf[LocalDateTime]) {
  val DateTimeRegex = """([\d]{4})/([\d]{2})/([\d]{2})T([\d]{2}):([\d]{2}):([\d]{2})""".r

  def fromString(value: String): Option[LocalDateTime] = value.trim() match {
    case DateTimeRegex(year, month, day, hour, minute, second) => Some(new LocalDateTime(year.toInt, month.toInt, day.toInt, hour.toInt, minute.toInt, second.toInt))
    case _ => None
  }
}
