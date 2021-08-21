package ru.school21.turing.descriptions

import org.json4s._
import org.json4s.jackson.Serialization.writePretty

case class Description[T](
                         name: Option[String],
                         alphabet: Option[List[String]],
                         blank: Option[String],
                         states: Option[List[String]],
                         initial: Option[String],
                         finals: Option[List[String]],
                         transitions: Option[T]
                       )
{
  override def toString: String = writePretty(this)(DefaultFormats)
}
