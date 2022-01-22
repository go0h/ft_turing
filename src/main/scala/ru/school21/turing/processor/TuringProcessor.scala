package ru.school21.turing.processor

import scala.annotation.tailrec
import ru.school21.turing.descriptions.{Description, Transition}

class TuringProcessor(description: Description, tape: Tape, verbose: Boolean = true) {

  if ((tape.tape.toSet -- description.alphabet.get).nonEmpty) {
    throw new IllegalArgumentException(
      s"Input symbol '${(tape.tape.toSet -- description.alphabet.get).mkString(", ")}' not in " +
        s"alphabet '${description.alphabet.get.mkString(", ")}'"
    )
  }

  def process(): String = {

    if (verbose) println(description)

    process(getTransition(description.initial, tape(0)), description.initial, 0)
    tape.getResult
  }

  @tailrec
  private def process(transition: Transition, transitionName: Option[String], pos: Int): Unit = {
    if (verbose) {
      println(s"${tape.createStr(pos)} ${transition.getTransitionString(transitionName)}")
    }
    tape.apply(transition, pos)
    if (!description.isFinals(transition.toState)) {
      val nextPos =
        if (transition.action.get.toLowerCase == "left") pos - 1
        else pos + 1
      process(getTransition(transition.toState, tape(nextPos)), transition.toState, nextPos)
    }
  }

  def getTransition(name: Option[String], read: String): Transition =
    description.getTransition(name, read)
}

object TuringProcessor {

  def apply(description: Description, input: Tape, verbose: Boolean = true): TuringProcessor =
    new TuringProcessor(description, input, verbose)
}
