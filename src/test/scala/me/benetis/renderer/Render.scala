package me.benetis.renderer

import me.benetis.model.Post
import me.benetis.partials.MainPartial
import zio._
import zio.console.Console

object Render {

  def render(posts: Set[Post]): ZIO[Console, Nothing, Unit] =
    for {
      _ <- ZIO.collectAll(
        posts.map(renderPage)
      )
    } yield ()

  def renderPage(post: Post): ZIO[Console, Nothing, Unit] = {
    for {
      output <- UIO(MainPartial.render(post))
      _      <- console.putStrLn(output)
    } yield ()
  }

}
