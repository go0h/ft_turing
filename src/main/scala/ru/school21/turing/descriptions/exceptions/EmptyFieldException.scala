package ru.school21.turing.descriptions.exceptions

class EmptyFieldException(field: String, className: String) extends Exception {

  override def getMessage: String = s"Field $field is empty in class $className"

}
