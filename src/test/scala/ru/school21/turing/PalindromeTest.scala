package ru.school21.turing

import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.descriptions.Description
import ru.school21.turing.processor.TuringProcessor
import ru.school21.turing.processor.TuringProcessorTest.getParsedDescription

class PalindromeTest extends AnyFunSuite {

  val palindrome: Description = getParsedDescription("resources/palindrome.json")

  def getResult(input: String): String = {
    TuringProcessor(palindrome, input, verbose = false)
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
    assert(getResult("aaaaaaaaaaabaaaaaa") == "n")
  }

  test("Bad Even Palindrome - aaaa") {
    assert(getResult("aaab") == "n")
  }

  test("Bad Even Palindrome - abbaabaaabba") {
    assert(getResult("abbaabaaabba") == "n")
  }

  test("Correct Odd Palindrome - a") {
    assert(getResult("a") == "y")
  }

  test("Correct Odd Palindrome - aaa") {
    assert(getResult("aaa") == "y")
  }

  test("Correct Odd Palindrome - aba") {
    assert(getResult("aba") == "y")
  }

  test("Correct Odd Palindrome - abbabba") {
    assert(getResult("abbabba") == "y")
  }

  test("Correct Odd Palindrome - abbbbbbaabaabbbbbba") {
    assert(getResult("abbbbbbaabaabbbbbba") == "y")
  }

  test("Bad Odd Palindrome - abbbbbbaabbabbbbbba") {
    assert(getResult("abbbbbbaabbabbbbbba") == "n")
  }

  test("Bad Odd Palindrome - aaabbaa") {
    assert(getResult("aaabbaa") == "n")
  }
}
