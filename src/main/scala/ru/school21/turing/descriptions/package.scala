package ru.school21.turing

import org.json4s.JField

package object descriptions {

  val transformFields: PartialFunction[JField, JField] = {
    case ("to_state", x) => ("toState", x)
  }
}
