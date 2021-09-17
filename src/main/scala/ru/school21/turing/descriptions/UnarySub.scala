package ru.school21.turing.descriptions

import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.writePretty
import ru.school21.turing.descriptions.exceptions._

case class UnarySub(
                     scanright: Option[List[Transition]],
                     eraseone: Option[List[Transition]],
                     subone: Option[List[Transition]],
                     skip: Option[List[Transition]]
                   ) extends Validated
{
  override def toString: String = writePretty(this)(DefaultFormats)

  override def checkFieldsType(): Unit = {
    getClass.getDeclaredFields.foreach {
      field => print(s"${getClass.getSimpleName} ${field.getName}: ")
        field.setAccessible(true)
        field.get(this) match {
          case Some(value) =>
            value match {
              case transitions: List[_] => println("List[Transition]")
                if (transitions.isEmpty)
                  throw new EmptyFieldException(field.getName, getClass.getSimpleName)
                else
                  transitions.foreach(x => {
                    if (!x.isInstanceOf[Transition])
                      throw new WrongFieldTypeException(field, Transition.getClass)
                    x.asInstanceOf[Transition].checkFieldsType()
                    }
                  )
              case wrong => throw new WrongFieldTypeException(field, wrong.getClass)
            }
          case None => throw new EmptyFieldException(field.getName, getClass.getSimpleName)
        }
    }
  }
}
