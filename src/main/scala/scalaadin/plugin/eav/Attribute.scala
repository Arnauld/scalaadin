package scalaadin.plugin.eav

class Attribute(val namespace:Namespace, val attributeName:String) {

  def qualifiedName = namespace.qualifiedName + "@" + attributeName
  
}