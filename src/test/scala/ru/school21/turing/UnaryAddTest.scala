package ru.school21.turing

import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.descriptions.Description
import ru.school21.turing.processor.TuringProcessor
import ru.school21.turing.processor.TuringProcessorTest.getParsedDescription

class UnaryAddTest extends AnyFunSuite {

  val unaryAdd: Description = getParsedDescription("resources/unary_add.json")

  test("Correct Unary_Add - 11+11=") {
    assert(TuringProcessor(unaryAdd, "11+11=", verbose = false)
      .process() == "1111")
  }

  test("Correct Unary_Add - 1+1=") {
    assert(TuringProcessor(unaryAdd, "1+1=", verbose = false)
      .process() == "11")
  }

  test("Correct Unary_Add - 1+1111111=") {
    assert(TuringProcessor(unaryAdd, "1+1111111=", verbose = false)
      .process() == "11111111")
  }

  test("Correct Unary_Add - 111=") {
    assert(TuringProcessor(unaryAdd, "111=", verbose = false)
      .process() == "111")
  }

  test("Correct Unary_Add - 1..1.+..1..1=") {
    assert(TuringProcessor(unaryAdd, "1..1.+..1..1=", verbose = false)
      .process() == "1..1.1..1")
  }

  test("Without equal - 11+11") {
    assertThrows[IndexOutOfBoundsException] {
      TuringProcessor(unaryAdd, "11+11", verbose = false).process()
    }
  }

  test("Without equal - 11") {
    assertThrows[IndexOutOfBoundsException] {
      TuringProcessor(unaryAdd, "11", verbose = false).process()
    }
  }
}