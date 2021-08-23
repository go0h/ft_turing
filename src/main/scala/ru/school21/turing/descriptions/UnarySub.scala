package ru.school21.turing.descriptions

import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.writePretty

case class UnarySub(
                     scanright: Option[List[Transition]],
                     eraseone: Option[List[Transition]],
                     subone: Option[List[Transition]],
                     skip: Option[List[Transition]]
                   ) extends Validated
{
  override def toString: String = writePretty(this)(DefaultFormats)
}
