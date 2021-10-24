package ru.school21.turing

import scala.util.{Try, Using}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import ru.school21.turing.descriptions.{Description, transformFields}
import scopt.OParser

object Main {

  def main(args: Array[String]): Unit = {

    val config: Config = OParser.parse(parser, args, Config()) match {
      case Some(config) => println(config)
        config
      case _ => System.exit(1)
        Config()
    }

    try {
      val json: Try[String] = Using(io.Source.fromFile(config.description)) {
        reader => reader.getLines().reduce(_ + _)
      }
      val jsonString = json.get

      implicit val formats: Formats = DefaultFormats

      val description = parse(jsonString)
        .transformField(transformFields)
        .extract[Description]
        .validate()

      println(description)
    } catch {
      case e: Exception => println(e.getMessage)
    }
  }
}