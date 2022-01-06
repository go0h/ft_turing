package ru.school21.turing.descriptions.exceptions

import java.lang.reflect.Field

class WrongFieldTypeException(field: Field, need: Class[_]) extends Exception {

  override def getMessage: String =
    s"Wrong field ${field.getName}: Need type $need, get ${field.getType}"

}
