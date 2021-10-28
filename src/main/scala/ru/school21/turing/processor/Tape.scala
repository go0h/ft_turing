package ru.school21.turing.processor

import ru.school21.turing.descriptions.Transition

import scala.language.implicitConversions

class Tape(in: String) {

  private var pos = 0

  if (in == null || in.trim.isEmpty)
    throw new RuntimeException("Input string is empty")

  val tape: Array[String] = (in + ("." * ((if (in.length + 2 < 30) 30 else in.length + 5) - in.length)))
    .map(_.toString).toArray

  def shift(direction: String): Unit = {
    direction.toLowerCase match {
      case "left" => pos -= 1
      case "right" => pos += 1
      case _ => throw new IllegalArgumentException(s"Wrong direction: $direction")
    }

//    if (pos < 0 || pos >= tape.length)
//      throw new IndexOutOfBoundsException
  }

  def cur: String = tape(pos)
  def cur(s: String): Unit = tape(pos) = s

  def apply(transitions: Seq[Transition]): Unit = { }

  def apply(transition: Transition): Unit = {
    tape(pos) = transition.write.get
    shift(transition.action.get)
  }

  override def toString: String = {
    "[" + tape.take(pos).mkString("") + "<" + tape(pos) + ">" + tape.slice(pos + 1, tape.length).mkString("") + "]"
  }
}

object Tape {

  def apply(input: String): Tape = {
    new Tape(input)
  }

  implicit def toCourse(inputString: String): Tape = Tape(inputString)
}