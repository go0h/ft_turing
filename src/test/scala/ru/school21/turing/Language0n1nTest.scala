package ru.school21.turing

import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.descriptions.Description
import ru.school21.turing.descriptions.exceptions.TuringLogicException
import ru.school21.turing.processor.TuringProcessor
import ru.school21.turing.processor.TuringProcessorTest.getParsedDescription

class Language0n1nTest extends AnyFunSuite {

  val language0n1n: Description = getParsedDescription("resources/language_0n1n.json")

  def getResult(input: String): String =
    TuringProcessor(language0n1n, input, verbose = false)
      .process()
      .replaceAll("^[.]++", "")

  test("Correct Language 0n1n - 01") {
    assert(getResult("01") == "y")
  }

  test("Correct Language 0n1n - 0011") {
    assert(getResult("0011") == "y")
  }

  test("Correct Language 0n1n - 00001111") {
    assert(getResult("00001111") == "y")
  }

  test("Correct Language 0n1n - 00000001111111") {
    assert(getResult("00000001111111") == "y")
  }

  test("Correct Language 0n1n - .01") {
    assert(getResult(".01") == "y")
  }

  test("Correct Language 0n1n - .....00001111") {
    assert(getResult(".....00001111") == "y")
  }

  test("Bad Language 0n1n - 0000001111111") {
    assert(getResult("0000001111111") == "n")
  }

  test("Bad Language 0n1n - 1111111") {
    assert(getResult("1111111") == "n")
  }

  test("Bad Language 0n1n - 110011") {
    assert(getResult("110011") == "n")
  }

  test("Bad Language 0n1n - 00101") {
    assert(getResult("00101") == "n")
  }

  test("Bad Language 0n1n - 0000.011011") {
    assert(getResult("0000.011011") == "n.011011")
  }

  test("Bad Language 0n1n - 01y") {
    assertThrows[TuringLogicException](getResult("01y"))
  }
}
