package ru.school21.turing.descriptions

import com.fasterxml.jackson.core.JsonParseException

import scala.io.Source
import org.json4s.jackson.JsonMethods.parse
import org.json4s.{DefaultFormats, Formats}
import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.descriptions.exceptions._

class DescriptionTest extends AnyFunSuite {

  implicit val formats: Formats = DefaultFormats

  def getJSONString(filename: String): String = {
    Source.fromResource(filename)
      .getLines()
      .reduce(_ + _)
  }

  def getParsedDescription(filename: String): Description = {
    val json = getJSONString(filename)
    parse(json)
      .transformField(transformFields)
      .extract[Description]
  }

  test("Bad JSON - 1") {
    val json = getJSONString("bad/unary_sub_1.json")
    assertThrows[JsonParseException] {
      parse(json).transformField(transformFields)
        .extract[Description]
    }
  }

  test("Empty field - 1") {
    assertThrows[EmptyFieldException] {
      getParsedDescription("bad/empty_field_1.json")
        .checkEmptyFields()
    }
  }

  test("Empty field - 2") {
    assertThrows[EmptyFieldException] {
      getParsedDescription("bad/empty_field_2.json")
        .checkEmptyFields()
    }
  }

  test("Empty string") {
    assertThrows[EmptyFieldException] {
      getParsedDescription("bad/empty_field_3.json")
        .checkEmptyFields()
    }
  }

  test("Empty field List[Transitions]") {
    assertThrows[EmptyFieldException] {
      getParsedDescription("bad/empty_list.json")
        .checkEmptyFields()
    }
  }

  test("Empty JSON") {
    assertThrows[EmptyFieldException] {
      getParsedDescription("empty.json")
        .checkEmptyFields()
    }
  }

  test("Check blank") {

    val description = Description(
      name = Option("unary_sub"),
      alphabet = Option(List("blank", "init")),
      blank = Option("blnk"),
      states = Option(List("final", "int1")),
      initial = Option("int"),
      finals = Option(List("final")),
      transitions = Option(Map())
    )

    assertThrows[TuringLogicException](
      description.checkBlank()
    )
  }

  test("Check initial") {

    val description = Description(
      name = Option("unary_sub"),
      alphabet = Option(List("blank", "init")),
      blank = Option("blank"),
      states = Option(List("final", "int1")),
      initial = Option("int"),
      finals = Option(List("final")),
      transitions = Option(Map())
    )

    assertThrows[TuringLogicException](
      description.checkInitial()
    )
  }

  test("Check finals") {

    val description = Description(
      name = Option("unary_sub"),
      alphabet = Option(List("blank", "init")),
      blank = Option("blank"),
      states = Option(List("final1", "int1")),
      initial = Option("int"),
      finals = Option(List("final")),
      transitions = Option(Map())
    )

    assertThrows[TuringLogicException](
      description.checkFinals()
    )
  }

  test("Check READ and WRITE states") {

    val description = Description(
      name = Option("unary_sub"),
      alphabet = Option(List("blank", "init")),
      blank = Option("blank"),
      states = Option(List("final1", "int1")),
      initial = Option("int"),
      finals = Option(List("final")),
      transitions = Option(Map())
    )

    assertThrows[TuringLogicException](
      description.checkTransitions()
    )
  }

  test("Bad read state - 1") {
    assertThrows[TuringLogicException] {
      val res = getParsedDescription("bad/bad_read_state.json")

      res.checkTransitions()
    }
  }

  test("Bad write state - 1") {
    assertThrows[TuringLogicException] {
      val res = getParsedDescription("bad/bad_write_state.json")

      res.checkTransitions()
    }
  }

  test("Bad to_state") {
    assertThrows[TuringLogicException] {
      val res = getParsedDescription("bad/bad_to_state.json")

      res.checkTransitions()
    }
  }

  test("Bad action") {
    assertThrows[TuringLogicException] {
      val res = getParsedDescription("bad/bad_action.json")

      res.checkTransitions()
    }
  }

  test("Has no finals") {
    assertThrows[TuringLogicException] {
      val res = getParsedDescription("bad/has_no_finals.json")

      res.checkTransitions()
    }
  }

  test("Wrong action") {
    assertThrows[TuringLogicException] {
      val res = getParsedDescription("bad/wrong_action_1.json")

      res.checkTransitions()
    }
  }
}
