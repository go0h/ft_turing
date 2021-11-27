package ru.school21.turing.processor

import ru.school21.turing.descriptions.Transition

import scala.language.implicitConversions

class Tape(in: String) {

  private var pos = 0
  val tape: Array[String] = createTapeFromInput

  def cur: String = tape(pos)

  def cur(s: String): Unit = tape(pos) = s

  def apply(transitions: Seq[Transition]): Unit = { }

  def apply(transition: Transition): Unit = {
    tape(pos) = transition.write.get
    shift(transition.action.get)
  }

  def getResult: String = tape.mkString("").replaceAll("[.]+$", "")

  private def createTapeFromInput: Array[String] = {

    val shortedIn = if (in.contains(";;")) in.substring(0, in.indexOf(";;")) else in

    if (shortedIn == null || shortedIn.trim.isEmpty)
      throw new IllegalArgumentException("Tape is empty")

    (shortedIn + ("." * ((if (shortedIn.length + 2 < 30) 30 else shortedIn.length + 5) - shortedIn.length)))
      .map(_.toString).toArray
  }

  private def shift(direction: String): Unit = {
    direction.toLowerCase match {
      case "left" => pos -= 1
      case "right" => pos += 1
      case _ => throw new IllegalArgumentException(s"Wrong direction: $direction")
    }

    if (pos < 0 || pos >= tape.length)
      throw new IndexOutOfBoundsException(s"Error: End of tape. Position = $pos")
  }

  override def toString: String = {
    "[" + tape.take(pos).mkString("") + "<" + tape(pos) + ">" + tape.slice(pos + 1, tape.length).mkString("") + "]"
  }
}

object Tape {

  def apply(input: String): Tape = new Tape(input)

  implicit def toCourse(inputString: String): Tape = Tape(inputString)
}