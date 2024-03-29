package ru.school21.turing.descriptions

import org.json4s.jackson.JsonMethods.parse
import org.json4s.jackson.Serialization.writePretty
import org.json4s.{DefaultFormats, Formats}
import ru.school21.turing.descriptions.exceptions._

import scala.util.{Try, Using}

final case class Description(
  name: Option[String],
  alphabet: Option[List[String]],
  blank: Option[String],
  states: Option[List[String]],
  initial: Option[String],
  finals: Option[List[String]],
  transitions: Option[Map[String, List[Transition]]]
) {

  def validate(): Description = {
    checkEmptyFields()
    checkBlank()
    checkInitial()
    checkFinals()
    checkTransitions()
    this
  }

  def checkBlank(): Unit = {

    val blnk   = blank.get
    val alphbt = alphabet.get

    if (!alphbt.contains(blnk))
      throw new TuringLogicException(
        s"Blank symbol '$blnk' not in alphabet '${alphbt.mkString(", ")}'"
      )
  }

  def checkInitial(): Unit = {

    val init = initial.get
    val stts = states.get

    if (!stts.contains(init))
      throw new TuringLogicException(
        s"Initial state '$init' not in allowed states '${stts.mkString(", ")}'"
      )
  }

  def checkFinals(): Unit = {

    val fnls = finals.get
    val stts = states.get

    fnls.foreach { finalState =>
      if (!stts.contains(finalState))
        throw new TuringLogicException(
          s"Final state '$finalState' not in allowed states '${stts.mkString(", ")}'"
        )
    }
  }

  def checkTransitions(): Unit = {

    val alphbt = alphabet.get
    val stts   = states.get
    val trnsts = transitions.get
    val fnls   = finals.get

    trnsts.foreach { field =>
      if (!stts.contains(field._1))
        throw new TuringLogicException(
          s"Transition '${field._1}' not in allowed states '${stts.mkString(", ")}'"
        )
      field._2 match {
        case list: List[_] if list.nonEmpty => list.foreach(_.checkTransitions(field._1, alphbt, stts))
        case _                              =>
      }
    }

    trnsts.values
      .reduce(_ ++ _)
      .map(_.toState)
      .find(tr => fnls.contains(tr.get))
      .getOrElse(throw new TuringLogicException(s"Transitions has no final state '${finals.mkString(", ")}'"))
  }

  def getTransitions(name: Option[String]): List[Transition] = transitions.get(name.getOrElse(""))

  def getTransition(name: Option[String], read: String): Transition =
    getTransitions(name)
      .find(_.read.get.equals(read))
      .getOrElse(throw new TuringLogicException(s"The machine is stacked on symbol '$read'"))

  def isFinals(transitionName: Option[String]): Boolean =
    finals.get.contains(transitionName.getOrElse(""))

  override def toString: String = {
    val width = name.get.length
    s"""${"*" * 80}
       |*${" " * 78}*
       |*${" " * (39 - width / 2 - width % 2)}${name.get}${" " * (39 - width / 2)}*
       |*${" " * 78}*
       |${"*" * 80}
       |Alphabet: ${alphabet.get.mkString("[ ", ", ", " ]")}
       |States:   ${states.get.mkString("[ ", ", ", " ]")}
       |Initial:  ${initial.get}
       |Finals:   ${finals.get.mkString("[ ", ", ", " ]")}
       |${"*" * 80}
       |${transitions.get.flatMap(x => x._2.map(_.getTransitionString(Option(x._1)))).mkString("\n")}
       |${"*" * 80}""".stripMargin
  }

  def checkEmptyFields(): Unit =
    getClass.getDeclaredFields.foreach { field =>
      field.setAccessible(true)
      field.get(this) match {
        case Some(value) =>
          value match {
            case string: String if string.trim.nonEmpty =>
            case list: List[_] if list.nonEmpty =>
              list.foreach {
                case state: String if state.trim.nonEmpty =>
                case _                                    => throw new EmptyFieldException(field.getName, getClass.getSimpleName)
              }
            case map: Map[_, _] if map.nonEmpty =>
              map.foreach { x =>
                x._2 match {
                  case transitions: List[_] if transitions.nonEmpty =>
                    transitions.foreach {
                      case tr: Transition => tr.checkEmptyFields()
                      case _              => throw new EmptyFieldException(x._1.toString, getClass.getSimpleName)
                    }
                  case _ => throw new EmptyFieldException(x._1.toString, getClass.getSimpleName)
                }
              }
            case _ => throw new EmptyFieldException(field.getName, getClass.getSimpleName)
          }
        case _ => throw new EmptyFieldException(field.getName, getClass.getSimpleName)
      }
    }

  def toJSON: String = writePretty(this)(DefaultFormats)
}

object Description {

  def apply(name: String,
            alphabet: List[String],
            blank: String,
            states: List[String],
            initial: String,
            finals: List[String],
            transitions: Map[String, List[Transition]]): Description =
    new Description(
      Option(name),
      Option(alphabet),
      Option(blank),
      Option(states),
      Option(initial),
      Option(finals),
      Option(transitions)
    )

  def readDescription(filename: String): Description = {

    val json: Try[String] = Using(io.Source.fromFile(filename)) { reader =>
      reader.getLines().reduce(_ + _)
    }
    val jsonString = json.get

    implicit val formats: Formats = DefaultFormats

    parse(jsonString)
      .transformField(transformFields)
      .extract[Description]
      .validate()
  }
}
