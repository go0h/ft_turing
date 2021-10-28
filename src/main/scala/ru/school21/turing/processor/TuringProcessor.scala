package ru.school21.turing.processor

import ru.school21.turing.descriptions.{Description, Transition}

class TuringProcessor(description: Description, tape: Tape) {

  if ((tape.tape.toSet -- description.alphabet.get).nonEmpty) {
    throw new IllegalArgumentException(
      s"Input symbol '${(tape.tape.toSet -- description.alphabet.get).mkString(", ")}' not in " +
        s"alphabet '${description.alphabet.get.mkString(", ")}'"
    )
  }

  def process(): Unit = {

    var transitionName: String = description.initial.get
    var transition: Transition = description.getTransition(transitionName, tape.cur)

    while(!description.isFinals(transition.toState.get)) {

      println(s"$tape ${transition.getTransitionString(transitionName)}")
      tape.apply(transition)

      transitionName = transition.toState.get
      transition = description.getTransition(transitionName, tape.cur)
    }
    println(s"$tape ${transition.getTransitionString(transitionName)}")
  }
}

object TuringProcessor {

  def apply(description: Description, input: Tape): TuringProcessor = {
    new TuringProcessor(description, input)
  }
}