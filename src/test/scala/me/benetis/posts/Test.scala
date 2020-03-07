package me.benetis.posts

import me.benetis.Content
import me.benetis.model.{Post, PostParams, Slug}
import me.benetis.partials.ContactMe
import scalatags.Text.all._
import me.benetis.utils.Extensions._
object Test extends Post {

  override val params: PostParams =
    PostParams("test", Slug("slug"), Vector.empty)

  def render(): Content = {
    div(
      p("some specific content here mate"),
      ContactMe.render()
    )
  }
}
