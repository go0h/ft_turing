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

    process(description.getTransition(description.initial, tape.cur), description.initial)
    tape.getResult
  }

  @tailrec
  private def process(transition: Transition, transitionName: Option[String]): Unit = {
    if (verbose) {
      println(s"$tape ${transition.getTransitionString(transitionName)}")
    }
    tape.apply(transition)
    if (!description.isFinals(transition.toState)) {
      process(description.getTransition(transition.toState, tape.cur), transition.toState)
    }
  }
}

object TuringProcessor {

  def apply(description: Description, input: Tape, verbose: Boolean = true): TuringProcessor = {
    new TuringProcessor(description, input, verbose)
  }
}