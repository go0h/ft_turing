package ru.school21.turing

import scala.util.Using
import ru.school21.turing.descriptions.Description
import ru.school21.turing.descriptions.gen.UniversalTuringMachineGenerator.createAndSaveUniversalDescriptions
import ru.school21.turing.processor.TuringProcessor
import scopt.OParser

object Main {

  def main(args: Array[String]): Unit = {

    val config: Config = OParser.parse(parser, args, Config()) match {
      case Some(config) => config
      case _ =>
        System.exit(1)
        Config()
    }

    try {
      val description = Description.readDescription(config.description)

      if (config.gen) {
        createAndSaveUniversalDescriptions(description)
      } else {

        val input =
          if (config.input == null) Using(io.Source.stdin)(_.getLines().reduce(_ + _)).get
          else config.input

        val res = TuringProcessor(description, input)
          .process()
        println(res)
      }
    } catch {
      case e: Exception => println(e.getMessage)
    }
  }
}
