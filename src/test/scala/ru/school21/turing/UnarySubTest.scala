package ru.school21.turing

import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.descriptions.Description
import ru.school21.turing.processor.TuringProcessor
import ru.school21.turing.processor.TuringProcessorTest.getParsedDescription

class UnarySubTest extends AnyFunSuite {

  val unarySub: Description = getParsedDescription("resources/unary_sub.json")

  test("Correct Unary_Sub - 11-11=") {
    assert(TuringProcessor(unarySub, "11-11=", verbose = false).process().equals(""))
  }

  test("Correct Unary_Sub - 1111-11=") {
    assert(TuringProcessor(unarySub, "1111-11=", verbose = false).process().equals("11"))
  }

  test("Correct Unary_Sub - BIG") {
    assert(
      TuringProcessor(
        unarySub,
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
          "1111111111111111111111111111111111111111111111111111111111111111111111111111111=",
        verbose = false
      ).process()
        .equals("1")
    )
  }

  test("Correct Unary_Sub - 111-111=") {
    assert(TuringProcessor(unarySub, "111-11=", verbose = false).process().equals("1"))
  }

  test("Correct Unary_Sub - 11111-11111=") {
    assert(TuringProcessor(unarySub, "11111-11111=", verbose = false).process().equals(""))
  }

  test("Correct Unary_Sub - 111..1..1-1111=") {
    assert(TuringProcessor(unarySub, "111..1..1-1111=", verbose = false).process().equals("1"))
  }
}
