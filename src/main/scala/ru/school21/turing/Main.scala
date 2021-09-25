package ru.school21.turing

import com.fasterxml.jackson.core.JsonParseException
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.util.{Try, Using}
import ru.school21.turing.descriptions.{Description, transformFields}

import java.io.FileNotFoundException

object Main {

  def main(args: Array[String]): Unit = {

    if (args.length == 0) {
      println("Usage: ft_turing")
      System.exit(1)
    }
    println(s"Filename: ${args.head}")

    try {
      val json: Try[String] = Using(io.Source.fromFile(args(0))) {
        reader => reader.getLines().reduce(_ + _)
      }
      val jsonString = json.get

      implicit val formats: Formats = DefaultFormats

      val description = parse(jsonString)
        .transformField(transformFields)
        .extract[Description[String]]
        .parseTransitions()

      println(description)
      description.validate()
    } catch {
      case e: FileNotFoundException => println(e.getMessage)
      case e: JsonParseException => println(e.getMessage)
    }
  }

}