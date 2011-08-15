package scalaadin.spath

import scalaadin.util.Invokers

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
              case arr: Array[_] => arr.asInstanceOf[Array[AnyRef]](x.value)
              case list: List[_] => list.asInstanceOf[List[AnyRef]](x.value)
              case list: java.util.List[_] => list.asInstanceOf[java.util.List[AnyRef]].get(x.value)
              case _ => throw new IllegalArgumentException("Value is neither a list nor an array for " + x + " got: " + prev)
            }
          case s: StrLiteral =>
            prev match {
              case map: Map[_, _] => map.asInstanceOf[Map[AnyRef, AnyRef]](s.value)
              case map: java.util.Map[_, _] => map.asInstanceOf[java.util.Map[AnyRef, AnyRef]].get(s.value)
              case _ => throw new IllegalArgumentException("Value is not a map for " + s + " got: " + prev)
            }
          case unknown => throw new IllegalArgumentException("Unknown literal type " + unknown)
        }
    })
    Some(applied)
  }
}

object SPathFragment {
  def apply(identifier: String):SPathFragment = AccessorRaw(identifier)
  def apply(identifier: String, indices: List[Literal]):SPathFragment = AccessorWithIndices(identifier, indices)

  case class AccessorRaw(identifier: String) extends SPathFragment

  case class AccessorWithIndices(identifier: String, override val indices: List[Literal]) extends SPathFragment
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