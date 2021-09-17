package ru.school21.turing.descriptions

import org.json4s._
import org.json4s.jackson.Serialization.writePretty
import ru.school21.turing.descriptions.exceptions.TuringLogicException

case class Description[T](
                         name: Option[String],
                         alphabet: Option[List[String]],
                         blank: Option[String],
                         states: Option[List[String]],
                         initial: Option[String],
                         finals: Option[List[String]],
                         transitions: Option[T]
                       ) extends Validated
{
  override def toString: String = writePretty(this)(DefaultFormats)

  def validate(): Unit = {
    checkFieldsType()
    checkBlank()
    checkInitial()
    checkFinals()
    checkTransitionsNames()
  }

  def checkBlank(): Unit = {

    val blnk = blank.get
    val alphbt = alphabet.get

    if (!alphbt.contains(blnk))
      throw new TuringLogicException(
        s"Blank symbol '$blnk' not in alphabet '${alphbt.mkString(", ")}'"
      )
    println("Check blank OK")
  }

  def checkInitial(): Unit = {

    val init = initial.get
    val stts = states.get

    if (!stts.contains(init))
      throw new TuringLogicException(
        s"Initial state '$init' not in allowed states '${stts.mkString(", ")}'"
      )
    println("Check initial OK")
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
    println("Check finals OK")
  }

  def checkTransitionsNames(): Unit = {

    val stts = states.get

    transitions.get
      .getClass.getFields
      .map(_.getName)
      .foreach(field => {
        if (!stts.contains(field))
          throw new TuringLogicException(
            s"Transition '$field' not in allowed states '${stts.mkString(", ")}'"
          )
        }
      )
    println("Check Transitions names OK")
  }
}
