package ru.school21.turing

import org.json4s._
import org.json4s.jackson.JsonMethods._
import scala.util.{Try, Using}
import ru.school21.turing.descriptions.{Description, UnarySub}

object Main {

  def main(args: Array[String]): Unit = {

    if (args.length == 0) {
      println("Usage: ft_turing")
      System.exit(1)
    }

    println(s"Filename: ${args(0)}")

    val json: Try[String] = Using(io.Source.fromFile(args(0))) {
      reader => reader.getLines().reduce(_ + _)
    }

    if (json.isFailure) {
      println(json)
      System.exit(1)
    }

    val jsonString = json.get

    implicit val formats: Formats = DefaultFormats

    val description = parse(jsonString).extract[Description[UnarySub]]
    println(description)

  }

}