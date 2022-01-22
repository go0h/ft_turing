package ru.school21.turing.processor

import ru.school21.turing.descriptions.Transition
import ru.school21.turing.processor.Tape.{RED, RESET}

import scala.language.implicitConversions

class Tape(in: String) {

  val tape: Array[String] = createTapeFromInput

  def apply(i: Int): String = tape(i)

  def apply(transition: Transition, pos: Int): Unit = tape(pos) = transition.write.get

  def getResult: String = tape.mkString("").replaceAll("[.]+$", "")

  private def createTapeFromInput: Array[String] = {

    val shortedIn = if (in.contains(";;")) in.substring(0, in.indexOf(";;")) else in

    if (shortedIn == null || shortedIn.trim.isEmpty)
      throw new IllegalArgumentException("Tape is empty")

    (shortedIn + ("." * ((if (shortedIn.length + 2 < 30) 30 else shortedIn.length + 5) - shortedIn.length)))
      .map(_.toString)
      .toArray
  }

  def createStr(pos: Int): String =
    "[" + tape.take(pos).mkString("") + RED + tape(pos) + RESET + tape.slice(pos + 1, tape.length).mkString("") + "]"
}

object Tape {

  val RESET = "\u001B[0m"
  val RED   = "\u001B[41m"

  def apply(input: String): Tape = new Tape(input)

  implicit def toCourse(inputString: String): Tape = Tape(inputString)
}
