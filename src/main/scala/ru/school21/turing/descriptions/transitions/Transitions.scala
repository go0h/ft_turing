package ru.school21.turing.descriptions.transitions

import ru.school21.turing.descriptions.JsonStruct
import ru.school21.turing.descriptions.exceptions._

trait Transitions extends JsonStruct {

  override def checkFieldsType(): Unit = {
    getClass.getDeclaredFields.foreach { field =>
      field.setAccessible(true)
      field.get(this) match {
        case Some(value) =>
          value match {
            case transitions: List[_] =>
              if (transitions.isEmpty)
                throw new EmptyFieldException(field.getName, getClass.getSimpleName)
              else
                transitions.foreach { x =>
                  if (!x.isInstanceOf[Transition])
                    throw new WrongFieldTypeException(field, Transition.getClass)
                  x.asInstanceOf[Transition].checkFieldsType()
                }
            case wrong => throw new WrongFieldTypeException(field, wrong.getClass)
          }
        case None => throw new EmptyFieldException(field.getName, getClass.getSimpleName)
      }
    }
  }

  override def toString: String = {
    getClass.getDeclaredFields.map { field =>
      field.setAccessible(true)
      field.get(this) match {
        case Some(value) =>
          val name = field.getName
          value.asInstanceOf[List[Transition]]
            .map(_.getTransitionString(name))
            .mkString("\n")
        case _ => s"${field.getName}: NONE"
      }
    }.mkString("\n")
  }
}
