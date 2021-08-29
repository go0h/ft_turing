package ru.school21.turing.descriptions

import com.fasterxml.jackson.core.JsonParseException

import scala.io.Source
import org.json4s.jackson.JsonMethods.parse
import org.json4s.{DefaultFormats, Formats}
import org.scalatest.funsuite.AnyFunSuite

class DescriptionTest extends AnyFunSuite {

  test("Bad JSON - 1") {
    val json = Source.fromResource("unary_sub_1.json")
      .getLines
      .reduce(_ + _)

    implicit val formats: Formats = DefaultFormats

    assertThrows[JsonParseException] {
      parse(json).transformField(transformFields)
        .extract[Description[UnarySub]]
    }
  }

  test("Empty JSON") {
    val json = Source.fromResource("empty.json")
      .getLines
      .reduce(_ + _)

    implicit val formats: Formats = DefaultFormats
    parse(json).transformField(transformFields)
      .extract[Description[UnarySub]]
  }

}
