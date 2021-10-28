package ru.school21.turing.processor

import ru.school21.turing.descriptions.{Description, Transition}

class TuringProcessor(description: Description, input: String) {
  def process(): Unit = {


  }
}

object TuringProcessor {
  def apply(description: Description, input: String): TuringProcessor = {
    new TuringProcessor(description, input)
  }
}