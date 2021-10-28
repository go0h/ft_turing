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

  test("Wrong input char - 1") {
    val description = getParsedDescription("resources/unary_sub.json")
    assertThrows[IllegalArgumentException]{
      TuringProcessor(description, "1112-1=")
    }
  }
}
