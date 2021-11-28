package ru.school21.turing

import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.descriptions.Description
import ru.school21.turing.processor.TuringProcessor
import ru.school21.turing.processor.TuringProcessorTest.getParsedDescription

class LanguageNTest extends AnyFunSuite {

  val languageN: Description = getParsedDescription("resources/language_n.json")

  def getResult(input: String): String = {
    TuringProcessor(languageN, input, verbose = false)
      .process()
      .replaceAll("^[.]++", "")
  }

  test("Correct Language N - 01") {
    assert(getResult("01") == "y")
  }

  test("Correct Language N - 0011") {
    assert(getResult("0011") == "y")
  }

  test("Correct Language N - 00001111") {
    assert(getResult("00001111") == "y")
  }

  test("Correct Language N - 00000001111111") {
    assert(getResult("00000001111111") == "y")
  }

  test("Correct Language N - .01") {
    assert(getResult(".01") == "y")
  }

  test("Correct Language N - .....00001111") {
    assert(getResult(".....00001111") == "y")
  }

  test("Bad Language N - 0000001111111") {
    assert(getResult("0000001111111") == "n")
  }

  test("Bad Language N - 1111111") {
    assert(getResult("1111111") == "n")
  }

  test("Bad Language N - 110011") {
    assert(getResult("110011") == "n")
  }

  test("Bad Language N - 00101") {
    assert(getResult("00101") == "n")
  }

  test("Bad Language N - 0000.011011") {
    assert(getResult("0000.011011") == "n.011011")
  }
}
