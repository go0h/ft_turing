package ru.school21.turing.gen

import ru.school21.turing.descriptions.{Description, Transition}

import java.io.PrintWriter
import scala.:+
import scala.collection.mutable
import scala.util.Using

object UTM {

  /**
   * Original - unary_add_simple.json
   * S[ ] - states, S - state_name
   * { } - transitions
   * {.S.R} - { "read" : ".", "to_state": "scanright", "write": ".", "action": "RIGHT"}
   *
   * scanright - S[{1S1R}{+A.R}{.H.L}]
   * addone -    A[{[1W+L}{.H.L]}]
   * writeone -  W[{.S1R}]
   *
   * Пример кодировки S!S[{1S1R}{+A.R}{.H.L}]A[{[1W+L}{.H.L]}]W[{.S1R}]
   * S - начальное состояние
   * ALPHABET - ! { } [ ] R L S A W H 1 . +
   *
   */

  val NAME = "Universal Turing Machine"

  val LEFT = "L"
  val RIGHT = "R"
  val HALT = "H"
  val PTR = "*"
  val BLANK = "_"
  val SEP = "#"
  val START = "!"
  val STATE_BR = List("[", "]")
  val TR_BR = List("{", "}")
  val SERVICE: List[String] = STATE_BR ++ TR_BR ++ List(START, LEFT, RIGHT, HALT, BLANK, PTR, SEP)

  /* s!s[{1s1R}{+a.R}{.H.L}]a[{1w+L}{.H.L}]w[{.s1R}] */
  def generateString(description: Description): String = {
    val trans = description.transitions.get
    val tr = trans.map { state =>
      state._1 + "[" + state._2.map(t => s"{${t.shortNotation}}").mkString + "]"
    }.mkString
    description.initial.get + START + tr
  }

  def main(args: Array[String]): Unit = {

    println(args(0))
    val description = Description.readDescription(args(0))

    val name = NAME + ": " + description.name.get
    val input = description.alphabet.get :+ BLANK
    val states = description.states.get
    val alphabet = input ++ states ++ SERVICE
    val blank = description.blank.get

    val initial = "init"

    val prodStIn: List[(String, String)] = states.flatMap(s => input.map(i => (s, i)))

    println(generateString(description))

    val transitions: mutable.HashMap[String, List[Transition]] =
      new mutable.HashMap[String, List[Transition]]() ++
      Map(initial -> {
        states.map(state => Transition(state, s"go_to_null_$state", state, "RIGHT"))
        }) ++
      states.map { state => {
        s"go_to_null_$state" ->
          alphabet.map { a =>
            if (a != SEP) Transition(a, s"go_to_null_$state", a, "RIGHT")
            else Transition(SEP, s"go_to_state_$state", SEP, "RIGHT")
          }
        }
      } ++
      states.map { state =>
        s"go_to_state_$state" ->
          (if (state != HALT) {
            input.map { in =>
              Transition(in, s"init_find_$state-${if (in != blank) in else  BLANK}", PTR, "LEFT")
            }
          } else {
            input.map { in => Transition(in, "HALT", in, "RIGHT") }
          })
      } ++
      prodStIn.map { x =>
        val (state, in) = x
        s"init_find_$state-$in" ->
          alphabet.map { a =>
            if (a != START) Transition(a, s"init_find_$state-$in", a, "LEFT")
            else Transition(START, s"find_state_$state($in)", START, "RIGHT")
          }
      } ++
      prodStIn.map { x =>
        val (state, in) = x
        s"find_state_$state($in)" ->
          ((input ++ SERVICE).filter(_ != SEP).map { a =>
            Transition(a, s"find_state_$state($in)", a, "RIGHT")
          } ++
          states.filter(_ != state).map { a =>
            Transition(a, s"find_state_$state($in)", a, "RIGHT")
          } :+
          Transition(state, s"check_op_$state-($in)", state, "RIGHT"))
      } ++
      prodStIn.map { x =>
        val (state, in) = x
        s"check_op_$state-($in)" ->
          (alphabet.map { a =>
            Transition(a, s"find_state_$state($in)", a, "RIGHT")
          } :+
          Transition("{", s"cmp_read_$state($in)", "{", "RIGHT"))
      } ++
      prodStIn.map { x =>
        val (state, in) = x
        s"cmp_read_$state($in)" ->
          alphabet.filter(_ != "}").map { a =>
            if (a == in) Transition(in, s"get_state_$in", in, "RIGHT")
            else if (a == "[") Transition("[", s"cmp_read_$state($in)", "[", "RIGHT")
            else Transition(a, s"to_next_trans_$state($in)", a, "RIGHT")
        }
      } ++
      input.map { in =>
        s"get_state_$in" ->
          states.map { s => Transition(s, s"get_dir_$s", s, "RIGHT") }
      } ++
      states.map { state =>
        s"get_dir_$state" ->
          List(RIGHT, LEFT).map { dir => Transition(dir, s"get_write_$state:$dir", dir, "RIGHT") }
      } ++
      states.flatMap(s => List(RIGHT, LEFT).map(i => (s, i))).map { x =>
        val (state, dir) = x
        s"get_write_$state:$dir" ->
          input.map { in => Transition(in, s"eval_$dir($in)~$state", in, "RIGHT") }
      } ++
      prodStIn.map { x =>
        val (state, in) = x
        s"to_next_trans_$state($in)" ->
          alphabet.filter(_ != "}").map { a =>
            if (a == "]") Transition("]", s"cmp_read_$state($in)", "]", "RIGHT")
            else Transition(a, s"to_next_trans_$state($in)", a, "RIGHT")
          }
      } ++
      states.flatMap(s => List(RIGHT, LEFT).flatMap(dir => input.map(in => (s, dir, in)))).map { x =>
        val (state, dir, in) = x
        s"eval_$dir($in)~$state" ->
          alphabet.map { a =>
            if (a != PTR) Transition(a, s"eval_$dir($in)~$state", a, "RIGHT")
            else Transition(PTR, s"eval_$dir($in)~$state", PTR, if (a == RIGHT) "RIGHT" else "LEFT")
          }
      }

    val res = Description(
      name = name,
      alphabet = alphabet,
      blank = "_",
      states = transitions.keys.toList.sorted :+ "HALT",
      initial = initial,
      finals = List("HALT"),
      transitions = transitions.toMap)

    println(res.states.get.size)
//    println(res.states.get.foreach(println))


    val filename = s"./resources/utm_${description.name.get.replaceAll(" ", "_")}.json"
    Using(new PrintWriter(filename))(_.write(res.toJSON))
  }

}
