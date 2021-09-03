package ru.school21.turing.descriptions

import ru.school21.turing.descriptions.exceptions._

trait Validated {

  def validate(): Unit = {
    getClass.getDeclaredFields.foreach {
      field =>
        print(s"${getClass.getSimpleName} ${field.getName}: ")
        field.setAccessible(true)
        field.get(this) match {
          case Some(value) =>
            value match {
              case validated: Validated => validated.validate()
              case string: String => println("String")
                if (string.trim.isEmpty)
                  throw new EmptyFieldException(field.getName, getClass.getSimpleName)
              case list: List[String] => println("List[String]")
                if (list.isEmpty)
                  throw new EmptyFieldException(field.getName, getClass.getSimpleName)
                else
                  list.foreach(x =>
                    if (x.trim.isEmpty) throw new EmptyFieldException(field.getName, getClass.getSimpleName))
              case wrong => throw new WrongFieldTypeException(field, wrong.getClass)
            }
          case None => throw new EmptyFieldException(field.getName, getClass.getSimpleName)
        }
    }
  }
}
