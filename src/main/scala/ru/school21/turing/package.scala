package ru.school21

import scopt.{OParser, OParserBuilder}

package object turing {

  case class Config(
                     jsonFile: String = "",
                     input: String = "",
                     verbose: Boolean = false,
                     debug: Boolean = false)

  val builder: OParserBuilder[Config] = OParser.builder[Config]
  val parser: OParser[Unit, Config] = {
    import builder._
    OParser.sequence(
      programName("ft_turing"),
      arg[String]("jsonFile")
        .required()
        .action((x, c) => c.copy(jsonFile = x))
        .text("json description of the machine"),
      arg[String]("input")
        .optional()
        .action((x, c) => c.copy(input = x))
        .text("input of the machine"),
      help('h', "help")
        .text("show this help message and exit")
    )
  }

}
