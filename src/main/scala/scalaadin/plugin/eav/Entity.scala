package scalaadin.plugin.eav

class Entity(val namespace:Namespace, val entityName:String) {
  private var attributes = Map.empty[String,Attribute]
  def << (attribute:Attribute):Entity = {
    attributes += (attribute.qualifiedName -> attribute)
    this
  }

  def qualifiedName = namespace.qualifiedName + "$" + entityName
}