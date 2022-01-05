package ru.school21.turing

import scala.collection.mutable
import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.descriptions.Description
import ru.school21.turing.descriptions.exceptions.TuringLogicException
import ru.school21.turing.processor.TuringProcessor
import ru.school21.turing.processor.TuringProcessorTest.getParsedDescription
import ru.school21.turing.descriptions.gen.UniversalTuringMachineGenerator.{SEP, createSimplifiedDescription, createUniversalDescription, generateShortDescription}

class UniversalTuringMachineTest extends AnyFunSuite {

  val descriptions = new mutable.HashMap[String, (Description, String)]()

  def addUniversalDescription(name: String, filename: String): Unit = {
    val description: Description = getParsedDescription(filename)
    val utd = createUniversalDescription(description)
    val shortDescription = s"${generateShortDescription(createSimplifiedDescription(description))}$SEP"

    descriptions.addOne(name -> (utd, shortDescription))
  }

  val UNARY_ADD = "unary_add"
  val UNARY_SUB = "unary_sub"
  val PALINDROME = "palindrome"

  addUniversalDescription(UNARY_ADD, s"./resources/unary_add.json")
  addUniversalDescription(UNARY_SUB, s"./resources/unary_sub.json")
  addUniversalDescription(PALINDROME, s"./resources/palindrome.json")

  def getResult(name: String, input: String): String = {
    val (description, inputP1) = descriptions(name)

    Option(TuringProcessor(description, s"$inputP1$input", verbose = false)
      .process().split("#", 2)(1)).getOrElse("")
      .replaceAll("^[.]++", "")
  }

  test("UTM Correct Unary_Add - 11+11=") {
    assert(getResult(UNARY_ADD, "11+11=") == "1111")
  }

  test("UTM Correct Unary_Add - 1+1=") {
    assert(getResult(UNARY_ADD, "1+1=") == "11")
  }

  test("UTM Correct Unary_Add - 1+1111111=") {
    assert(getResult(UNARY_ADD, "1+1111111=") == "11111111")
  }

  test("UTM Correct Unary_Add - 111=") {
    assert(getResult(UNARY_ADD, "111=") == "111")
  }

  test("UTM Without equal - 11+11") {
    assertThrows[IndexOutOfBoundsException] {
      getResult(UNARY_ADD, "11+11")
    }
  }

  test("UTM Without equal - 11") {
    assertThrows[IndexOutOfBoundsException] {
      getResult(UNARY_ADD, "11")
    }
  }

  test("UTM Correct Unary_Sub - 11-11=") {
    assert(getResult(UNARY_SUB, "11-11=") == "")
  }

  test("UTM Correct Unary_Sub - 1111-11=") {
    assert(getResult(UNARY_SUB, "1111-11=") == "11")
  }

  /*
  test("UTM Correct Unary_Sub - BIG") {
    assert(getResult(UNARY_SUB,
      "1111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "-111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111111111111111111111111111111111111111111111111111111111111111111111111111=") == "1")
  }
   */

  test("UTM Correct Unary_Sub - 111-11=") {
    assert(getResult(UNARY_SUB, "111-11=") == "1")
  }

  test("UTM Correct Unary_Sub - 11111-11111=") {
    assert(getResult(UNARY_SUB, "11111-11111=") == "")
  }

  test("UTM Correct Unary_Sub - 111..1..1-1111=") {
    assert(getResult(UNARY_SUB, "111..1..1-1111=") == "1")
  }

  test("UTM Correct Even Palindrome - aa") {
    assert(getResult(PALINDROME, "aa") == "y")
  }

  test("UTM Correct Even Palindrome - bb") {
    assert(getResult(PALINDROME, "bb") == "y")
  }

  test("UTM Correct Even Palindrome - aaaa") {
    assert(getResult(PALINDROME, "aaaa") == "y")
  }

  test("UTM Correct Even Palindrome - abba") {
    assert(getResult(PALINDROME, "abba") == "y")
  }

  test("UTM Correct Even Palindrome - abbaabbaabba") {
    assert(getResult(PALINDROME, "abbaabbaabba") == "y")
  }

  test("UTM Correct Even Palindrome - aaaaaaaaaaaaaaaaaa") {
    assert(getResult(PALINDROME, "aaaaaaaaaaaaaaaaaa") == "y")
  }

  test("Bad Even Palindrome - aaaaaaaaaaabaaaaaa") {
    assert(getResult(PALINDROME, "aaaaaaaaaaabaaaaaa") == "n")
  }

  test("Bad Even Palindrome - aaaa") {
    assert(getResult(PALINDROME, "aaab") == "n")
  }

  test("Bad Even Palindrome - abbaabaaabba") {
    assert(getResult(PALINDROME, "abbaabaaabba") == "n")
  }

  test("UTM Correct Odd Palindrome - a") {
    assert(getResult(PALINDROME, "a") == "y")
  }

  test("UTM Correct Odd Palindrome - aaa") {
    assert(getResult(PALINDROME, "aaa") == "y")
  }

  test("UTM Correct Odd Palindrome - aba") {
    assert(getResult(PALINDROME, "aba") == "y")
  }

  test("UTM Correct Odd Palindrome - abbabba") {
    assert(getResult(PALINDROME, "abbabba") == "y")
  }

  test("UTM Correct Odd Palindrome - abbbbbbaabaabbbbbba") {
    assert(getResult(PALINDROME, "abbbbbbaabaabbbbbba") == "y")
  }

  test("UTM Bad Odd Palindrome - abbbbbbaabbabbbbbba") {
    assert(getResult(PALINDROME, "abbbbbbaabbabbbbbba") == "n")
  }

  test("UTM Bad Odd Palindrome - aaabbaa") {
    assert(getResult(PALINDROME, "aaabbaa") == "n")
  }
}
