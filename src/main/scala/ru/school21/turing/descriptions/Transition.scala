package ru.school21.turing.descriptions

import org.json4s._
import org.json4s.jackson.Serialization.writePretty

case class Transition(
                       read: Option[String],
                       toState: Option[String],
                       write: Option[String],
                       action: Option[String]
                     ) extends Validated
{
  override def toString: String = writePretty(this)(DefaultFormats)
}
