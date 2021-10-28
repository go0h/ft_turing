package ru.school21.turing.descriptions

import ru.school21.turing.descriptions.exceptions._

case class Description(
                        name: Option[String],
                        alphabet: Option[List[String]],
                        blank: Option[String],
                        states: Option[List[String]],
                        initial: Option[String],
                        finals: Option[List[String]],
                        transitions: Option[Map[String, List[Transition]]]
                      ) extends JsonStruct {

  def validate(): Description = {
    checkEmptyFields()
    checkBlank()
    checkInitial()
    checkFinals()
    checkTransitions()
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

  def checkTransitions(): Unit = {

    val alphbt = alphabet.get
    val stts = states.get
    val trnsts = transitions.get
    val fnls = finals.get

    var hasFinals = false

    trnsts
      .foreach { field =>
        if (!stts.contains(field._1))
          throw new TuringLogicException(
            s"Transition '${field._1}' not in allowed states '${stts.mkString(", ")}'"
          )
        field._2 match {
          case list: List[_] =>
            list.foreach {
              transition: Transition =>
                transition.checkTransitions(field._1, alphbt, stts)
                hasFinals = fnls.contains(transition.toState.get) || hasFinals
            }
        }
      }

    if (!hasFinals)
      throw new TuringLogicException(
        s"Transitions has no final state '${finals.mkString(", ")}'"
      )
  }

  def getTransitions(name: Option[String]): List[Transition] = transitions.get(name.getOrElse(""))

  def getTransition(name: Option[String], read: String): Transition = {
    getTransitions(name)
      .filter(_.read.get.equals(read))
      .head
  }

  def isFinals(transitionName: Option[String]): Boolean = {
    finals.get.contains(transitionName.getOrElse(""))
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
       |${transitions.get.flatMap(x => x._2.map(_.getTransitionString(Option(x._1)))).mkString("\n")}
       |${"*" * 80}"""
      .stripMargin
  }
}
