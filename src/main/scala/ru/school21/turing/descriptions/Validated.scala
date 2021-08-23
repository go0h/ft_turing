package ru.school21.turing.descriptions

trait Validated {

  def validate(): Unit = {
    getClass.getDeclaredFields.foreach {
      x =>
        println(s"${getClass.getSimpleName} ${x.getName}")
        x.setAccessible(true)
        x.get(this) match {
          case Some(x) =>
            x match {
              case validated: Validated => validated.validate()
              case _ =>
            }
          case None => throw new IllegalArgumentException(s"Field ${x.getName} is empty in class ${this.getClass.getSimpleName}")
        }
    }
  }
}
