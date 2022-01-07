package ru.school21.turing.descriptions.gen

import ru.school21.turing.descriptions.{Description, Transition}
import UniversalTuringMachineGenerator.createAndSaveUniversalDescriptions

object UTM {

  def main(args: Array[String]): Unit = {

    val digits              = "10"
    val symbols             = "-+=.|"
    val alpha               = "abyn"
    val input               = (digits + symbols + alpha).map(_.toString)
    val states: Seq[String] = "ABCDEFGIJK".map(_.toString)

    val prodInput                         = input.flatMap(i1 => input.map(i2 => (i1, i2)))
    val prodStates: Seq[(String, String)] = states.flatMap(s1 => states.map(s2 => (s1, s2)))

    val transitions: Map[String, List[Transition]] = Map.empty[String, List[Transition]] ++
      prodStates.map { key =>
        key._1 ->
          (prodInput.map(in => Transition(in._1, key._2, in._2, "RIGHT")) ++
            prodInput.map(in => Transition(in._1, key._2, in._2, "LEFT"))).toList
      }

    val res = Description(
      name        = "UTM",
      alphabet    = input.toList,
      blank       = ".",
      states      = states.toList,
      initial     = states.head,
      finals      = List("H"),
      transitions = transitions
    )

    createAndSaveUniversalDescriptions(res)
  }
}
