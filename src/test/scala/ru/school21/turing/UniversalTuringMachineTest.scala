package ru.school21.turing

import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.descriptions.Description
import ru.school21.turing.descriptions.exceptions.TuringLogicException
import ru.school21.turing.processor.TuringProcessor
import ru.school21.turing.processor.TuringProcessorTest.getParsedDescription

class UniversalTuringMachineTest extends AnyFunSuite {

  def getUniversalDescription(original: String): Description = {

    val description: Description = getParsedDescription(s"resources/$original.json")

    description
  }


  def getResult(description: Description, input: String): String = {
    TuringProcessor(description, input, verbose = false)
      .process().split("#")(1)
  }
}
