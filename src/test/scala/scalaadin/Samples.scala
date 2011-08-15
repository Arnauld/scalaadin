package scalaadin

object Samples {
  def newPerson = {
    val address = new Address(221, "B Baker Street")
    val person = new Person("Sherlock")
    person.firstName = "Sherlock"
    person.lastName = "Holmes"
    person.phones = List(PhoneNumber("0600-0001"), PhoneNumber("0100-0002"), PhoneNumber("0400-0003"))
    person.setAddress(address)
    person.attributes = Map(
      ("Occupation" -> "Consulting detective"), //
      ("Family" -> "Mycroft Holmes"), //
      ("Nationality" -> "British")
    )
    person.tags = Array("detective", "british", "watson")
    person
  }
}

class Address(var num: Int, var street: String) {
  def format = num + " " + street
}

case class PhoneNumber(num: String)

class Person(val name: String) {
  var firstName: String = ""
  var lastName: String = ""
  private var addr: Address = null
  var phones: List[PhoneNumber] = Nil
  var attributes = Map.empty[String, AnyRef]
  var tags = Array.empty[String]

  def getAddress: Address = addr

  def setAddress(address: Address) {
    this.addr = address
  }

  def fullName = firstName + " " + lastName
}