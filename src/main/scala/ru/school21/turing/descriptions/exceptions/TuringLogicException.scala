package ru.school21.turing.descriptions.exceptions

class TuringLogicException(msg: String) extends Exception {
  override def getMessage: String = msg
}
