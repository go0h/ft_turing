package ru.school21.turing.descriptions

import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.writePretty
import ru.school21.turing.descriptions.exceptions._

trait JsonStruct {

  def checkEmptyFields(): Unit =
    getClass.getDeclaredFields.foreach { field =>
      field.setAccessible(true)
      field.get(this) match {
        case Some(value) =>
          value match {
            case jsonStruct: JsonStruct                 => jsonStruct.checkEmptyFields()
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
