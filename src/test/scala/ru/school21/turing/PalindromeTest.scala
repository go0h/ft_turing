package ru.school21.turing

import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.descriptions.Description
import ru.school21.turing.processor.TuringProcessor
import ru.school21.turing.processor.TuringProcessorTest.getParsedDescription

class PalindromeTest extends AnyFunSuite {

  val unarySub: Description = getParsedDescription("resources/palindrome.json")

  def getResult(input: String): String = {
    TuringProcessor(unarySub, input, verbose = false)
      .process()
      .replaceAll("^[.]++", "")
  }

  test("Correct Even Palindrome - aa") {
    assert(getResult("aa") == "y")
  }

  test("Correct Even Palindrome - bb") {
    assert(getResult("bb") == "y")
  }

  test("Correct Even Palindrome - aaaa") {
    assert(getResult("aaaa") == "y")
  }

  test("Correct Even Palindrome - abba") {
    assert(getResult("abba") == "y")
  }

  test("Correct Even Palindrome - abbaabbaabba") {
    assert(getResult("abbaabbaabba") == "y")
  }

  test("Correct Even Palindrome - aaaaaaaaaaaaaaaaaa") {
    assert(getResult("aaaaaaaaaaaaaaaaaa") == "y")
  }

  test("Bad Even Palindrome - aaaaaaaaaaabaaaaaa") {
    assert(getResult("aaaaaaaaaaabaaaaaa").last == 'n')
  }

  test("Bad Even Palindrome - aaaa") {
    assert(getResult("aaab").last == 'n')
  }

  test("Bad Even Palindrome - abbaabaaabba") {
    assert(getResult("abbaabaaabba").last == 'n')
  }

}
