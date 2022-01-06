package ru.school21.turing

import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.descriptions.Description
import ru.school21.turing.descriptions.exceptions.TuringLogicException
import ru.school21.turing.processor.TuringProcessor
import ru.school21.turing.processor.TuringProcessorTest.getParsedDescription

class Language02nTest extends AnyFunSuite {

  val language0n1n: Description = getParsedDescription("resources/language_02n.json")

  def getResult(input: String): String =
    TuringProcessor(language0n1n, input, verbose = false)
      .process()
      .replaceAll("^[0]++", "")

  def isPowerOfTwo(n: Int): Boolean = (n & (n - 1)) == 0

  test("Bad Language 0^(2n) - 0") {
    assert(getResult("0") == "y")
  }

  test("Bad Language 0^(2n) - .......0") {
    assertThrows[TuringLogicException](getResult(".......0"))
  }

  test("Correct Language 0^(2n) - 00") {
    assert(getResult("00") == "y")
  }

  test("Correct Language 0^(2n) - 00000000") {
    assert(getResult("00000000") == "y")
  }

  test("Correct Language 0^(2n) - 00000000000000000000000000000000") {
    assert(getResult("00000000000000000000000000000000") == "y")
  }

  test("Bad Language 0^(2n) - 000000000000000000000000000000000") {
    assert(getResult("000000000000000000000000000000000") == "n")
  }

  test("Check 0^(2n) from 1 to 4096") {
    (1 to 4096).foreach { n =>
      val str = "0" * n
      val res = getResult(str)
      if (isPowerOfTwo(n)) {
        assert(res == "y", n)
      } else {
        assert(res == "n", n)
      }
    }
  }

  test("Bad Language 0^(2n) - 00y00") {
    assertThrows[TuringLogicException](getResult("00y00"))
  }
}
