package me.benetis.model

import me.benetis.Content
import scalatags.Text
import scalatags.Text.all._

case class Slug(value: String)

case class PostParams(
    title: String,
    slug: Slug,
    tags: Vector[Tag]
)

trait Partial {
  def render(): Content
}

trait Post extends Partial {
  val params: PostParams
  def render(): Content
}
