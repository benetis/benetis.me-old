package me.benetis

import me.benetis.partials.ContactMe
import me.benetis.posts.Test
import me.benetis.renderer.Render
import zio.{App, IO, URIO, ZIO}
import zio.console._

object MyApp extends App {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    myAppLogic.fold(_ => 1, _ => 0)

  val myAppLogic: ZIO[Console, Unit, Unit] =
    for {
      _ <- putStrLn("Starting...")
      _ <- Render.render(Set(posts.Test))
      _ <- IO.fail()
    } yield ()
}
