package ru.school21.turing.processor

import scala.annotation.tailrec
import ru.school21.turing.descriptions.{Description, Transition}

class TuringProcessor(description: Description, tape: Tape) {

  if ((tape.tape.toSet -- description.alphabet.get).nonEmpty) {
    throw new IllegalArgumentException(
      s"Input symbol '${(tape.tape.toSet -- description.alphabet.get).mkString(", ")}' not in " +
        s"alphabet '${description.alphabet.get.mkString(", ")}'"
    )
  }

  def process(): Unit = {
    process(description.getTransition(description.initial, tape.cur), description.initial)
  }

  @tailrec
  private def process(transition: Transition, transitionName: Option[String]): Unit = {
    println(s"$tape ${transition.getTransitionString(transitionName)}")
    if (!description.isFinals(transition.toState)) {
      tape.apply(transition)
      process(description.getTransition(transition.toState, tape.cur), transition.toState)
    }
  }
}

object TuringProcessor {

  def apply(description: Description, input: Tape): TuringProcessor = {
    new TuringProcessor(description, input)
  }
}