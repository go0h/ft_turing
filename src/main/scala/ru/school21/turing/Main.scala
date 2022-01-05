package ru.school21.turing

import scopt.OParser
import ru.school21.turing.descriptions.Description
import ru.school21.turing.descriptions.gen.UniversalTuringMachineGenerator.createAndSaveUniversalDescriptions
import ru.school21.turing.processor.TuringProcessor

object Main {

  def main(args: Array[String]): Unit = {

    val config: Config = OParser.parse(parser, args, Config()) match {
      case Some(config) => config
      case _ => System.exit(1)
        Config()
    }

    try {

      val description = Description.readDescription(config.description)

      if (config.gen) {
        createAndSaveUniversalDescriptions(description)
      } else {
        val res = TuringProcessor(description, config.input)
          .process()
        println(res)
      }

    } catch {
      case e: Exception => println(e.getMessage)
    }
  }
}