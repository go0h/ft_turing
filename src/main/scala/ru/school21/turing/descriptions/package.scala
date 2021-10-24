package ru.school21.turing

import org.json4s.{DefaultFormats, JField}
import org.json4s.JsonAST.{JObject, JString}
import org.json4s.jackson.Serialization.writePretty

package object descriptions {

  val transformFields: PartialFunction[JField, JField] = {
    case ("to_state", x) => ("toState", x)
  }
}
