package ru.school21.turing.descriptions

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.writePretty
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
                       ) extends Validated
{
  override def toString: String = writePretty(this)(DefaultFormats)

  def parseTransitions(): Description[Transitions] = {

    val t = parse(transitions.get.asInstanceOf[String])
      .transformField(transformFields)

    implicit val formats: Formats = DefaultFormats

    val transition = name.get.toLowerCase match {
      case "unary_sub" => t.extract[UnarySub]
    }

    Description(name, alphabet, blank, states, initial, finals, Option(transition))
  }


  def validate(): Unit = {
    checkFieldsType()
    checkBlank()
    checkInitial()
    checkFinals()
    checkTransitions()
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

  def checkTransitions(): Unit = {

    val alphbt = alphabet.get
    val stts = states.get
    val trnsts = transitions.get

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
                transition
                  .asInstanceOf[Transition]
                  .checkTransitions(field.getName, alphbt, stts)
            }
          case None => throw new WrongFieldTypeException(field, List[String]().getClass)
        }
      }
  }
}
