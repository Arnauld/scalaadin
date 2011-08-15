package scalaadin.util

import java.lang.reflect.AccessibleObject

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

  /**
   * Return an invoker on public method with no-args whose name match <code>what</code> or
   * a JavaBean accessor with "get" or "is" prefix
   */
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

  /**
   * Return an invoker on public method with no-args whose name match <code>what</code>
   */
  def methodPublic: Invoker = (klazz: Class[_], what: String, value: AnyRef) => {
    val methodOpt = klazz.getMethods.find({ m=> (m.getName == what) && (m.getParameterTypes.length == 0)})
    if (methodOpt.isDefined)
      Some(methodOpt.get.invoke(value))
    else
      None
  }

  /**
   * Return an invoker on any (public or private) method with no-args whose name match <code>what</code>
   */
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

  private var securityException = false
  def ensureAccessible(accessibleObject:AccessibleObject) {
    try {
      // only try to make it accessible if one doesn't receive an security exception
      // on last attempts
      if(!securityException && !accessibleObject.isAccessible)
        accessibleObject.setAccessible(true)
    }
    catch {
      case e:SecurityException => securityException = true
    }
  }
}
