package me.benetis.partials

import me.benetis.model.{Post, PostParams}
import scalatags.Text
import scalatags.Text.all._

object MainPartial {

  private def template(post: Post): Text.TypedTag[String] = html(
    head(script("some script")),
    body(
      h1(post.params.title),
      post.render()
    )
  )

  def render(post: Post): String = "<!DOCTYPE html>" + template(post)

}
