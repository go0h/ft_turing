package ru.school21.turing.descriptions.transitions

case class UnarySub(
                     scanright: Option[List[Transition]],
                     eraseone: Option[List[Transition]],
                     subone: Option[List[Transition]],
                     skip: Option[List[Transition]]
                   ) extends Transitions

