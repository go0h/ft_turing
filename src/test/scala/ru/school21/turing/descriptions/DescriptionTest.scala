package ru.school21.turing.descriptions

import com.fasterxml.jackson.core.JsonParseException

import scala.io.Source
import org.json4s.jackson.JsonMethods.parse
import org.json4s.{DefaultFormats, Formats, MappingException}
import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.descriptions.exceptions._
import ru.school21.turing.descriptions.transitions.{Transition, UnarySub}

class DescriptionTest extends AnyFunSuite {

  implicit val formats: Formats = DefaultFormats

  val unarySubEmpty: UnarySub = UnarySub(
    Option(List(Transition(Option(""), Option(""), Option(""), Option("")))),
    Option(List(Transition(Option(""), Option(""), Option(""), Option("")))),
    Option(List(Transition(Option(""), Option(""), Option(""), Option("")))),
    Option(List(Transition(Option(""), Option(""), Option(""), Option("")))),
  )

  def getJSONString(filename: String): String = {
    Source.fromResource(filename)
      .getLines()
      .reduce(_ + _)
  }

  def getParsedDescription[T: Manifest](filename: String): Description[T] = {
    val json = getJSONString(filename)
    parse(json)
      .transformField(transformFields)
      .extract[Description[T]]
  }

  test("Bad JSON - 1") {
    val json = getJSONString("bad/unary_sub_1.json")
    assertThrows[JsonParseException] {
      parse(json).transformField(transformFields)
        .extract[Description[UnarySub]]
    }
  }

  test("Empty field - 1") {
    assertThrows[EmptyFieldException] {
      getParsedDescription[UnarySub]("bad/empty_field_1.json")
        .checkFieldsType()
    }
  }

  test("Empty field - 2") {
    assertThrows[EmptyFieldException] {
      getParsedDescription[UnarySub]("bad/empty_field_2.json")
        .checkFieldsType()
    }
  }

  test("Empty string") {
    assertThrows[EmptyFieldException] {
      getParsedDescription[UnarySub]("bad/empty_field_3.json")
        .checkFieldsType()
    }
  }

  test("Empty field List[Transitions]") {
    assertThrows[EmptyFieldException] {
      getParsedDescription[UnarySub]("bad/empty_list.json")
        .checkFieldsType()
    }
  }

  test("Empty JSON") {
    assertThrows[EmptyFieldException] {
      getParsedDescription[UnarySub]("empty.json")
        .checkFieldsType()
    }
  }

  test("Check blank") {

    val description = Description[UnarySub](
      name = Option("unary_sub"),
      alphabet = Option(List("blank", "init")),
      blank = Option("blnk"),
      states = Option(List("final", "int1")),
      initial = Option("int"),
      finals = Option(List("final")),
      transitions = Option(unarySubEmpty)
    )

    assertThrows[TuringLogicException](
      description.checkBlank()
    )
  }

  test("Check initial") {

    val description = Description[UnarySub](
      name = Option("unary_sub"),
      alphabet = Option(List("blank", "init")),
      blank = Option("blank"),
      states = Option(List("final", "int1")),
      initial = Option("int"),
      finals = Option(List("final")),
      transitions = Option(unarySubEmpty)
    )

    assertThrows[TuringLogicException](
      description.checkInitial()
    )
  }

  test("Check finals") {

    val description = Description[UnarySub](
      name = Option("unary_sub"),
      alphabet = Option(List("blank", "init")),
      blank = Option("blank"),
      states = Option(List("final1", "int1")),
      initial = Option("int"),
      finals = Option(List("final")),
      transitions = Option(unarySubEmpty)
    )

    assertThrows[TuringLogicException](
      description.checkFinals()
    )
  }

  test("Check READ and WRITE states") {

    val description = Description[UnarySub](
      name = Option("unary_sub"),
      alphabet = Option(List("blank", "init")),
      blank = Option("blank"),
      states = Option(List("final1", "int1")),
      initial = Option("int"),
      finals = Option(List("final")),
      transitions = Option(unarySubEmpty)
    )

    assertThrows[TuringLogicException](
      description.checkTransitions()
    )
  }

  test("Bad read state - 1") {
    assertThrows[TuringLogicException] {
      val res = getParsedDescription[String]("bad/bad_read_state.json")
        .parseTransitions

      res.checkTransitions()
    }
  }

  test("Bad write state - 1") {
    assertThrows[TuringLogicException] {
      val res = getParsedDescription[String]("bad/bad_write_state.json")
        .parseTransitions

      res.checkTransitions()
    }
  }

  test("Bad to_state") {
    assertThrows[TuringLogicException] {
      val res = getParsedDescription[String]("bad/bad_to_state.json")
        .parseTransitions

      res.checkTransitions()
    }
  }

  test("Bad action") {
    assertThrows[TuringLogicException] {
      val res = getParsedDescription[String]("bad/bad_action.json")
        .parseTransitions

      res.checkTransitions()
    }
  }

  test("Has no finals") {
    assertThrows[TuringLogicException] {
      val res = getParsedDescription[String]("bad/has_no_finals.json")
        .parseTransitions

      res.checkTransitions()
    }
  }
}
