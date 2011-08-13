package scalaadin.spath

import java.lang.reflect.AccessibleObject

sealed abstract class Literal

case class IntLiteral(value: Int) extends Literal

case class StrLiteral(value: String) extends Literal

case class Identifier(value: String)

trait SPathFragment {
  def identifier: String

  def indices: List[Literal] = Nil

  def invokers = Invokers.defaults

  def select(value: AnyRef): Option[AnyRef] = value match {
    case x: Map[_, _] =>
      x.asInstanceOf[Map[AnyRef, AnyRef]].get(identifier) match {
        case None => None
        case Some(v) => applyIndices(v)
      }
    case x =>
      val klazz = value.getClass
      for(invoker <- invokers) {
        val computedOpt = invoker(klazz, identifier, value)
        if(computedOpt.isDefined)
          return applyIndices(computedOpt.get)
      }
      None
  }

  def applyIndices(value: AnyRef): Option[AnyRef] = {
    val applied = indices.foldLeft(value)({
      (prev, index) =>
        index match {
          case x: IntLiteral =>
            prev match {
              case list: List[_] => list.asInstanceOf[List[AnyRef]](x.value)
              case list: java.util.List[_] => list.asInstanceOf[java.util.List[AnyRef]].get(x.value)
              case _ => throw new IllegalArgumentException("Value is not a list got: " + prev)
            }
          case s: StrLiteral =>
            prev match {
              case map: Map[_, _] => map.asInstanceOf[Map[AnyRef, AnyRef]](s.value)
              case map: java.util.Map[_, _] => map.asInstanceOf[java.util.Map[AnyRef, AnyRef]].get(s.value)
              case _ => throw new IllegalArgumentException("Value is not a map got: " + prev)
            }
          case unknown => throw new IllegalArgumentException("Unknown literal type " + unknown)
        }
    })
    Some(applied)
  }
}

object Invokers {
  type Invoker = (Class[_], String, AnyRef) => Option[AnyRef]

  val defaults = Array(fieldPublic, methodPublicOrGetter, fieldAny, methodAny)

  def fieldPublic: Invoker = (klazz: Class[_], what: String, value: AnyRef) => {
    val fieldOpt = klazz.getFields.find(_.getName == what)
    if (fieldOpt.isDefined)
      Some(fieldOpt.get.get(value))
    else
      None
  }

  def fieldAny: Invoker = (klazz: Class[_], what: String, value: AnyRef) => {
    val fieldOpt = klazz.getDeclaredFields.find(_.getName == what)
    if (fieldOpt.isDefined) {
      val field = fieldOpt.get
      ensureAccessible(field)
      Some(field.get(value))
    }
    else
      None
  }

  private def capitalize(value:String) = {
    value.charAt(0).toUpper + value.substring(1)
  }

  def methodPublicOrGetter: Invoker = (klazz: Class[_], what: String, value: AnyRef) => {
    val methodOpt = klazz.getMethods.find({ m=>
      val mName = m.getName
      (m.getParameterTypes.length == 0) &&
      (mName == what || m.getName == "get"+capitalize(what) || m.getName == "is"+capitalize(what))
    })
    if (methodOpt.isDefined)
      Some(methodOpt.get.invoke(value))
    else
      None
  }

  def methodPublic: Invoker = (klazz: Class[_], what: String, value: AnyRef) => {
    val methodOpt = klazz.getMethods.find({ m=> (m.getName == what) && (m.getParameterTypes.length == 0)})
    if (methodOpt.isDefined)
      Some(methodOpt.get.invoke(value))
    else
      None
  }

  def methodAny: Invoker = (klazz: Class[_], what: String, value: AnyRef) => {
    val methodOpt = klazz.getDeclaredMethods.find({ m=> (m.getName == what) && (m.getParameterTypes.length == 0)})
    if (methodOpt.isDefined) {
      val method = methodOpt.get
      ensureAccessible(method)
      Some(method.invoke(value))
    }
    else
      None
  }

  def ensureAccessible(accessibleObject:AccessibleObject) {
    if(!accessibleObject.isAccessible)
      accessibleObject.setAccessible(true)
  }
}

case class AccessorRaw(identifier: String) extends SPathFragment

case class AccessorWithIndices(identifier: String, override val indices: List[Literal]) extends SPathFragment

object SPathFragment {
  def apply(identifier: String):SPathFragment = AccessorRaw(identifier)
  def apply(identifier: String, indices: List[Literal]):SPathFragment = AccessorWithIndices(identifier, indices)
}

object SPath {
  def apply(parts: List[SPathFragment]): SPath = new SPath(parts)

  def apply(input: String): SPath = SPathParser(input).getOrElse({
    throw new IllegalArgumentException("Invalid path <" + input + ">")
  })
}

class SPath(val fragments: List[SPathFragment]) {
  def select(root: AnyRef): Option[AnyRef] = {
    var elem = root
    for (fragment <- fragments) {
      val next = fragment.select(elem)
      if (next.isDefined)
        elem = next.get
      else
        return None
    }
    Some(elem)
  }
}