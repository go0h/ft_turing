package ru.school21.turing

import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.descriptions.Description
import ru.school21.turing.descriptions.exceptions.TuringLogicException
import ru.school21.turing.processor.TuringProcessor
import ru.school21.turing.processor.TuringProcessorTest.getParsedDescription

class Language02nTest extends AnyFunSuite {

  val language0n1n: Description = getParsedDescription("resources/language_02n.json")

  def getResult(input: String): String = {
    TuringProcessor(language0n1n, input, verbose = false)
      .process()
      .replaceAll("^[.]++", "")
  }

  test("Bad Language 02n - 0") {
    assert(getResult("0") == "n")
  }

  test("Bad Language 02n - .......0") {
    assert(getResult(".......0") == "n")
  }

  test("Bad Language 02n - .......00") {
    assert(getResult(".......00") == "y")
  }

  test("Correct Language 02n - 00") {
    assert(getResult("00") == "y")
  }

  test("Correct Language 02n - 00000000") {
    assert(getResult("00000000") == "y")
  }

  test("Correct Language 02n - 00000000000000000000000000000000") {
    assert(getResult("00000000000000000000000000000000") == "y")
  }

  test("Correct Language 02n - 000.0000") {
    assert(getResult("000.0000") == "n0000")
  }

  test("Bad Language 02n - 000000000000000000000000000000000") {
    assert(getResult("000000000000000000000000000000000") == "n")
  }

  test("Bad Language 02n - 00y00") {
    assertThrows[TuringLogicException](getResult("00y00"))
  }

}
