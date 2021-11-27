package ru.school21.turing.processor

import scala.util.Using
import org.json4s.jackson.JsonMethods.parse
import org.json4s.{DefaultFormats, Formats}
import org.scalatest.funsuite.AnyFunSuite
import ru.school21.turing.processor.TuringProcessorTest._
import ru.school21.turing.descriptions.{Description, transformFields}

object TuringProcessorTest {

  implicit val formats: Formats = DefaultFormats

  def getJSONString(filename: String): String = {
    Using(io.Source.fromFile(filename)) {
      reader => reader.getLines().reduce(_ + _)
    }.getOrElse("")
  }

  def getParsedDescription(filename: String): Description = {
    val json = getJSONString(filename)
    parse(json)
      .transformField(transformFields)
      .extract[Description]
      .validate()
  }
}

class TuringProcessorTest extends AnyFunSuite {

  val unarySub: Description = getParsedDescription("resources/unary_sub.json")

  test("Wrong input char - 1") {
    assertThrows[IllegalArgumentException]{
      TuringProcessor(unarySub, "1112-1=")
    }
  }

  test("Wrong input char - 2") {
    assertThrows[IllegalArgumentException]{
      TuringProcessor(unarySub, "11;-1=")
    }
  }

  test("Out of bounds - 30") {
    assertThrows[IndexOutOfBoundsException]{
      TuringProcessor(unarySub, "11-11", verbose = false)
        .process()
    }
  }

  test("Out of bounds - 31") {
    assertThrows[IndexOutOfBoundsException]{
      TuringProcessor(unarySub, "11-111=", verbose = false)
        .process()
    }
  }

  test("Test OK - ;;") {
    TuringProcessor(unarySub, "111-11=;;32309irjfdslkfj", verbose = false)
      .process()
  }

  test("Empty Tape - 1") {
    assertThrows[IllegalArgumentException]{
      TuringProcessor(unarySub, "")
        .process()
    }
  }

  test("Empty Tape - 2") {
    assertThrows[IllegalArgumentException]{
      TuringProcessor(unarySub, ";;11-11=")
        .process()
    }
  }
}
