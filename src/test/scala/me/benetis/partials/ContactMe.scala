package me.benetis.partials

import me.benetis.Content
import me.benetis.model.Partial
import scalatags.Text.all._
import me.benetis.utils.Extensions._

object ContactMe extends Partial {
  def render(): Content = div(
    div("Contact me @ XX"),
    div("HMMMM")
  )
}
