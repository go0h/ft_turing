package ru.school21.turing.descriptions

import ru.school21.turing.descriptions.exceptions.{EmptyFieldException, TuringLogicException}

final case class Transition(
  read: Option[String],
  toState: Option[String],
  write: Option[String],
  action: Option[String]
) {

  def checkTransitions(field: String, alphabet: List[String], states: List[String]): Unit = {

    if (!alphabet.contains(write.get))
      throw new TuringLogicException(
        s"'write' field '${write.get}' in transition '$field' not in alphabet '${alphabet.mkString(", ")}'"
      )
    if (!alphabet.contains(read.get))
      throw new TuringLogicException(
        s"'read' field '${read.get}' in transition '$field' not in alphabet '${alphabet.mkString(", ")}'"
      )
    if (!states.contains(toState.get))
      throw new TuringLogicException(
        s"'to_state' field '${toState.get}' in transition '$field' not in states '${states.mkString(", ")}'"
      )
    if (!List("RIGHT", "LEFT").contains(action.get.toUpperCase()))
      throw new TuringLogicException(
        s"'action' field '${action.get}' in transition '$field' not in states 'RIGHT, LEFT'"
      )
  }

  def getTransitionString(name: Option[String]): String =
    s"(${name.get}, ${read.get}) -> (${toState.get}, ${write.get}, ${action.get})"

  def shortNotation: String = read.get + toState.get + write.get + action.get(0)

//  def checkEmptyFields(): Unit =
//    this.productIterator.exists {
//      case Some(str: String) if str.trim.nonEmpty => true
//      case _                                      => throw new EmptyFieldException("to_state", getClass.getSimpleName)
//    }

  def checkEmptyFields(): Unit = {
    if (read.getOrElse("").trim.isEmpty)
      throw new EmptyFieldException("read", getClass.getSimpleName)
    if (toState.getOrElse("").trim.isEmpty)
      throw new EmptyFieldException("to_state", getClass.getSimpleName)
    if (write.getOrElse("").trim.isEmpty)
      throw new EmptyFieldException("write", getClass.getSimpleName)
    if (action.getOrElse("").trim.isEmpty)
      throw new EmptyFieldException("action", getClass.getSimpleName)
  }
}

object Transition {

  def apply(read: String, toState: String, write: String, action: String): Transition =
    new Transition(Some(read), Some(toState), Some(write), Some(action))
}
