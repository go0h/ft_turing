package ru.school21.turing.descriptions

import com.fasterxml.jackson.core.JsonParseException

import scala.io.Source
import org.json4s.jackson.JsonMethods.parse
import org.json4s.{DefaultFormats, Formats}
import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.descriptions.exceptions._

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
      .getLines
      .reduce(_ + _)
  }

  def getParsedDescription[T : Manifest](filename: String): Description[T] = {
    val json = getJSONString(filename)
    parse(json).transformField(transformFields)
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
    val res = getParsedDescription[UnarySub]("bad/empty_field_1.json")
    assertThrows[EmptyFieldException] {
      res.checkFieldsType()
    }
  }

  test("Empty field - 2") {
    val res = getParsedDescription[UnarySub]("bad/empty_field_2.json")
    assertThrows[EmptyFieldException] {
      res.checkFieldsType()
    }
  }

  test("Empty string") {
    val res = getParsedDescription[UnarySub]("bad/empty_field_3.json")
    assertThrows[EmptyFieldException] {
      res.checkFieldsType()
    }
  }

  test("Empty field List[Transitions]") {
    val res = getParsedDescription[UnarySub]("bad/empty_list.json")
    assertThrows[EmptyFieldException] {
      res.checkFieldsType()
    }
  }

  test("Empty JSON") {
    val json = getJSONString("empty.json")
    val res = parse(json).transformField(transformFields)
      .extract[Description[UnarySub]]

    assertThrows[EmptyFieldException](res.checkFieldsType())
  }

  test("Check blank") {

    val description = Description[UnarySub](
      Option("unary_sub"),
      Option(List("blank", "init")),
      Option("blnk"),
      Option(List("final")),
      Option("init"),
      Option(List("final")),
      Option(unarySubEmpty)
    )

    assertThrows[TuringLogicException](
      description.checkBlank()
    )
  }

  test("Check initial") {

    val description = Description[UnarySub](
      Option("unary_sub"),
      Option(List("blank", "init")),
      Option("blank"),
      Option(List("final", "int")),
      Option("int"),
      Option(List("final")),
      Option(unarySubEmpty)
    )

    assertThrows[TuringLogicException](
      description.checkInitial()
    )
  }

  test("Check initial") {

    val description = Description[UnarySub](
      Option("unary_sub"),
      Option(List("blank", "init", "final")),
      Option("blank"),
      Option(List("final")),
      Option("int"),
      Option(List("finl")),
      Option(unarySubEmpty)
    )

    assertThrows[TuringLogicException](
      description.checkFinals()
    )
  }


}
