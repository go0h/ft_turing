package ru.school21.turing

import scala.util.{Try, Using}
import scopt.OParser
import org.json4s._
import org.json4s.jackson.JsonMethods._
import ru.school21.turing.descriptions.{Description, transformFields}
import ru.school21.turing.processor.TuringProcessor

object Main {

  def main(args: Array[String]): Unit = {

    val config: Config = OParser.parse(parser, args, Config()) match {
      case Some(config) => config
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
      val res = TuringProcessor(description, config.input)
        .process()
      println(res)
    } catch {
      case e: Exception => println(e.getMessage)
    }
  }
}