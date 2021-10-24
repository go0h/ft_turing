package ru.school21.turing.descriptions

import org.json4s._
import org.json4s.jackson.JsonMethods._
import ru.school21.turing.descriptions.exceptions._
import ru.school21.turing.descriptions.transitions._

case class Description[T](
                           name: Option[String],
                           alphabet: Option[List[String]],
                           blank: Option[String],
                           states: Option[List[String]],
                           initial: Option[String],
                           finals: Option[List[String]],
                           transitions: Option[T]
                         ) extends JsonStruct {

  def validate(): Description[T] = {
    checkFieldsType()
    checkBlank()
    checkInitial()
    checkFinals()
    this
  }

  def checkBlank(): Unit = {

    val blnk = blank.get
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

    fnls.foreach(finalState => {
      if (!stts.contains(finalState))
        throw new TuringLogicException(
          s"Final state '$finalState' not in allowed states '${stts.mkString(", ")}'"
        )
    })
  }

  def parseTransitions: Description[Transitions] = {

    this.validate()

    val t = parse(transitions.get.asInstanceOf[String])
      .transformField(transformFields)

    implicit val formats: Formats = DefaultFormats

    val transition = name.get.toLowerCase match {
      case "unary_sub" => t.extract[UnarySub]
      case x => throw new IllegalArgumentException(s"Can't find '$x' description")
    }
    val tr = transition.asInstanceOf[Transitions]

    Description(name, alphabet, blank, states, initial, finals, Option(tr))
      .validate()
  }

  def checkTransitions(): Unit = {

    val alphbt = alphabet.get
    val stts = states.get
    val trnsts = transitions.get
    val fnls = finals.get

    var hasFinals = false

    trnsts
      .getClass
      .getDeclaredFields
      .foreach { field =>
        field.setAccessible(true)

        if (!stts.contains(field.getName))
          throw new TuringLogicException(
            s"Transition '$field' not in allowed states '${stts.mkString(", ")}'"
          )
        field.get(trnsts) match {
          case Some(list: List[_]) =>
            list.foreach {
              transition =>
                val tr = transition.asInstanceOf[Transition]
                tr.checkTransitions(field.getName, alphbt, stts)

                hasFinals = fnls.contains(tr.toState.get) || hasFinals
            }
          case None => throw new WrongFieldTypeException(field, List[String]().getClass)
        }
      }

    val transitionsName = transitions.get.getClass.getSimpleName

    if (!hasFinals)
      throw new TuringLogicException(
        s"Transition '$transitionsName' has no final state '${finals.mkString(", ")}'"
      )
  }

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
       |${transitions.get.toString}
       |${"*" * 80}"""
      .stripMargin
  }
}
