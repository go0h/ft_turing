package ru.school21.turing.descriptions.transitions

import ru.school21.turing.descriptions.JsonStruct
import ru.school21.turing.descriptions.exceptions.TuringLogicException

case class Transition(
                       read: Option[String],
                       toState: Option[String],
                       write: Option[String],
                       action: Option[String]
                     ) extends JsonStruct {

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
        s"'action' field '${action.get}' in transition '$field' not in states '${states.mkString(", ")}'"
      )
  }

  def getTransitionString(name: String): String = {
    s"($name, ${read.get}) -> (${toState.get}, ${write.get}, ${action.get})"
  }
}
