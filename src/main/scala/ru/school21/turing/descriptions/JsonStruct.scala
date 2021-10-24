package ru.school21.turing.descriptions

import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.writePretty
import ru.school21.turing.descriptions.exceptions._

trait JsonStruct {

  def checkFieldsType(): Unit = {
    getClass.getDeclaredFields.foreach {
      field =>
        field.setAccessible(true)
        field.get(this) match {
          case Some(value) =>
            value match {
              case validated: JsonStruct => validated.checkFieldsType()
              case string: String =>
                if (string.trim.isEmpty)
                  throw new EmptyFieldException(field.getName, getClass.getSimpleName)
              case list: List[_] =>
                if (list.isEmpty)
                  throw new EmptyFieldException(field.getName, getClass.getSimpleName)
                else
                  list.foreach(x => {
                    if (!x.isInstanceOf[String])
                      throw new WrongFieldTypeException(field, getClass)
                    if (x.asInstanceOf[String].trim.isEmpty)
                      throw new EmptyFieldException(field.getName, getClass.getSimpleName)
                    }
                  )
              case wrong => throw new WrongFieldTypeException(field, wrong.getClass)
            }
          case None => throw new EmptyFieldException(field.getName, getClass.getSimpleName)
        }
    }
  }

  def toJSON: String = writePretty(this)(DefaultFormats)
}
