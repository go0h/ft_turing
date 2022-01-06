package ru.school21.turing.descriptions.gen

import ru.school21.turing.descriptions.{Description, Transition}

import java.io.PrintWriter
import scala.collection.mutable
import scala.util.Using

/** Generate Universal Turing Machine description for input description
  * All states rename to one char. Example for states
  * [ "first", "second", "third" ] renamed to [ "A', "B", "C" ]
  *
  * Generate short description which need to put in input to generated UTM description.
  * RESERVED CHARS - ! { } [ ] L R H * # _
  *
  * Example of short description:
  *    A!A[{.A.R}{1A1R}{+C+R}{=H.R}]C[{1B+L}{+C1R}{=D.L}{.C.R}]B[{+A1R}{.B.L}]D[{.D.L}{+D.L}{1H1L}]
  *    A - initial state
  *    A[ ] - state, A - state_name
  *    { } - transition. Example - {.A.R} - { "read" : ".", "to_state": "A", "write": ".", "action": "RIGHT"}
  */
object UniversalTuringMachineGenerator {

  val NAME = "Universal Turing Machine"

  val START                 = "!"
  val LEFT                  = "L"
  val RIGHT                 = "R"
  val HALT                  = "H"
  val PTR                   = "*"
  val BLANK                 = "_"
  val SEP                   = "#"
  val ST_BR_L               = "["
  val ST_BR_R               = "]"
  val TR_BR_L               = "{"
  val TR_BR_R               = "}"
  val SERVICE: List[String] = List(START, LEFT, RIGHT, HALT, PTR, SEP, ST_BR_L, ST_BR_R, TR_BR_L, TR_BR_R)

  val INIT_STATE = "init"

  val INSTRUCTIONS_NAMES: List[String] = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz"
    .map(_.toString)
    .toSet
    .removedAll(SERVICE)
    .toList
    .sorted

  def createAndSaveUniversalDescriptions(description: Description): Unit = {
    val name = s"utm_${description.name.get.replaceAll(" ", "_")}.json"
    val utm  = createUniversalDescription(description)
    saveDescriptionToFile(utm, name)
    printUsageForDescription(createSimplifiedDescription(description), name)
  }

  /**
    * @return Simplified Description.
    *         All transitions renamed to one char string from INSTRUCTIONS_NAMES.
    *         Example for description <b>unary_add</b>:
    *         transition <b>scanright</b> renamed to <b>A</b>, <b>addone</b> renamed to <b>B</b>, etc.
    *         Finals transition always renamed to <b>H</b>.
    */
  def createSimplifiedDescription(description: Description): Description = {

    val states = description.states.get.filter(_ != description.finals.get.head)

    val allowedInstructions = INSTRUCTIONS_NAMES.toSet
      .removedAll(description.alphabet.get)
      .toList

    if (states.length > allowedInstructions.length)
      throw new IllegalArgumentException(
        s"In description ${description.name.get} more than ${INSTRUCTIONS_NAMES.length} transitions. I can't simplified it."
      )
    if (description.finals.get.length > 1)
      throw new IllegalArgumentException(
        s"In description ${description.name.get} more than 1 finals states. I can't simplified it."
      )
    val intersect = description.alphabet.get.toSet.intersect(SERVICE.toSet)
    if (intersect.nonEmpty)
      throw new IllegalArgumentException(
        s"Description ${description.name.get} has reserved UTM characters '${intersect.mkString(", ")}' in alphabet"
      )

    val mapping: Map[String, String] = (states.zip(INSTRUCTIONS_NAMES) :+ (description.finals.get.head, "H")).toMap

    val shortTransitions = description.transitions.get.map { instr =>
      mapping(instr._1) ->
        instr._2.map { tr =>
          Transition(tr.read.get, mapping(tr.toState.get), tr.write.get, tr.action.get.head.toString)
        }
    }

    Description(
      name        = description.name.get + "_simple",
      alphabet    = description.alphabet.get,
      blank       = description.blank.get,
      states      = mapping.values.toList,
      initial     = mapping(description.initial.get),
      finals      = List(mapping(description.finals.get.head)),
      transitions = shortTransitions
    )
  }

  /** Return short description in order to run Universal Turing Machine */
  def generateShortDescription(description: Description): String = {

    val transitions = description.transitions.get.map { state =>
      state._1 + ST_BR_L + state._2.map(t => s"$TR_BR_L${t.shortNotation}$TR_BR_R").mkString + ST_BR_R
    }.mkString

    description.initial.get + START + transitions
  }

  def createUniversalDescription(descriptionFull: Description): Description = {

    val description = createSimplifiedDescription(descriptionFull)

    val name     = NAME + ": " + description.name.get
    val input    = description.alphabet.get :+ BLANK
    val states   = description.states.get
    val alphabet = input ++ states ++ SERVICE

    val prodStIn: List[(String, String)] = states.flatMap(s => input.map(i => (s, i)))

    val transitions: mutable.HashMap[String, List[Transition]] =
      new mutable.HashMap[String, List[Transition]]() ++
        Map(INIT_STATE -> states.map(state => Transition(state, s"go_to_first_char__$state", state, "RIGHT"))) ++
        /* go to first INPUT char with init state */
        states.map { state =>
          s"go_to_first_char__$state" ->
            alphabet.map { a =>
              if (a != SEP) Transition(a, s"go_to_first_char__$state", a, "RIGHT")
              else Transition(SEP, s"go_to_state__$state", SEP, "RIGHT")
            }
        } ++
        /* when character is found, then go to start of description */
        states.map { state =>
          s"go_to_state__$state" ->
            (if (state != HALT) input.map { in =>
               Transition(in, s"go_to_start__$state[$in]", PTR, "LEFT")
             } else
               input.map { in =>
                 Transition(in, "HALT", in, "RIGHT")
               })
        } ++
        /* go to start of description, to 'START'(!) character */
        prodStIn.map { x =>
          val (state, in) = x
          s"go_to_start__$state[$in]" ->
            alphabet.map { a =>
              if (a != START) Transition(a, s"go_to_start__$state[$in]", a, "LEFT")
              else Transition(START, s"find_state__$state[$in]", START, "RIGHT")
            }
        } ++
        /* find state in description */
        prodStIn.map { x =>
          val (state, in) = x
          s"find_state__$state[$in]" ->
            ((input ++ SERVICE).filter(_ != SEP).map { a =>
              Transition(a, s"find_state__$state[$in]", a, "RIGHT")
            } ++
              states.filter(_ != state).map { a =>
                Transition(a, s"find_state__$state[$in]", a, "RIGHT")
              } :+
              Transition(state, s"check_state__$state[$in]", state, "RIGHT"))
        } ++
        /* if 'state' char is found, then check char '[' at right, if OK, then check read char */
        prodStIn.map { x =>
          val (state, in) = x
          s"check_state__$state[$in]" ->
            alphabet.map { a =>
              if (a != ST_BR_L) Transition(a, s"find_state__$state[$in]", a, "RIGHT")
              else Transition(ST_BR_L, s"check_read_char__$state[$in]", ST_BR_L, "RIGHT")
            }
        } ++
        /* if read char is OK, go to get next transition, otherwise go to next transition  */
        prodStIn.map { x =>
          val (state, in) = x
          s"check_read_char__$state[$in]" ->
            alphabet.filter(_ != ST_BR_R).map { a =>
              if (a == in) Transition(in, s"get_next_state__$in", in, "RIGHT")
              else if (a == TR_BR_L) Transition(TR_BR_L, s"check_read_char__$state[$in]", TR_BR_L, "RIGHT")
              else Transition(a, s"go_to_next_trans__$state[$in]", a, "RIGHT")
            }
        } ++
        /* get direction from transition */
        input.map { in =>
          s"get_next_state__$in" -> states.map { state =>
            Transition(state, s"get_direction__$state", state, "RIGHT")
          }
        } ++
        /* get char which need to write in this transition */
        states.map { state =>
          s"get_direction__$state" ->
            (List(RIGHT, LEFT) ++ input).map { dir =>
              if (dir == LEFT || dir == RIGHT) Transition(dir, s"get_write_char__$state[$dir]", dir, "LEFT")
              else Transition(dir, s"get_direction__$state", dir, "RIGHT")
            }
        } ++
        /* go to pointer and do job (replace char) */
        states.flatMap(s => List(RIGHT, LEFT).map(i => (s, i))).map { x =>
          val (state, dir) = x
          s"get_write_char__$state[$dir]" ->
            input.map { in =>
              Transition(in, s"do_job__$state[$in]_then_$dir", in, "RIGHT")
            }
        } ++
        /* find transition in state */
        prodStIn.map { x =>
          val (state, in) = x
          s"go_to_next_trans__$state[$in]" ->
            alphabet.filter(_ != ST_BR_R).map { a =>
              if (a == TR_BR_R) Transition(TR_BR_R, s"check_read_char__$state[$in]", TR_BR_R, "RIGHT")
              else Transition(a, s"go_to_next_trans__$state[$in]", a, "RIGHT")
            }
        } ++
        /* go to char in input, and replace it, then go to next char */
        states.flatMap(s => List(RIGHT, LEFT).flatMap(dir => input.map(in => (s, dir, in)))).map { x =>
          val (state, dir, in) = x
          s"do_job__$state[$in]_then_$dir" ->
            alphabet.map { a =>
              if (a != PTR) Transition(a, s"do_job__$state[$in]_then_$dir", a, "RIGHT")
              else Transition(PTR, s"go_to_state__$state", in, if (dir == RIGHT) "RIGHT" else "LEFT")
            }
        }

    Description(
      name        = name,
      alphabet    = alphabet,
      blank       = "_",
      states      = transitions.keys.toList.sorted :+ "HALT",
      initial     = INIT_STATE,
      finals      = List("HALT"),
      transitions = transitions.toMap
    )
  }

  def saveDescriptionToFile(description: Description, filename: String = ""): String = {

    val name =
      if (filename.nonEmpty) filename
      else s"utm_${description.name.get.replaceAll(" ", "_")}.json"

    Using(new PrintWriter(name))(_.write(description.toJSON))

    name
  }

  def printUsageForDescription(description: Description, name: String): Unit =
    println(
      s"""
         |Created Universal Turing Machine - $name
         |Usage:
         |java -jar ft_turing.jar $name '${generateShortDescription(description)}${SEP}input'""".stripMargin
    )
}
