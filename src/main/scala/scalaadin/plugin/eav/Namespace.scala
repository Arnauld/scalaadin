package scalaadin.plugin.eav

case class Namespace(name: String, parent: Option[Namespace] = Some(Namespace.Root)) {
  // integrity
  if(parent.isEmpty && !name.equals("/"))
    throw new IllegalArgumentException("Only root namespace can have an empty parent")

  def hasParent = parent.isDefined

  def qualifiedName:String = qualifiedName(new StringBuilder).toString()

  private def qualifiedName(bldr:StringBuilder):StringBuilder = {
    if(parent.isDefined) {
      parent.get.qualifiedName(bldr)
      // prevent double '/' for root
      if(parent.get.hasParent)
        bldr.append('/')
    }
    bldr.append(name)
  }

}

object Namespace {
  val Root = Namespace("/", None)
}