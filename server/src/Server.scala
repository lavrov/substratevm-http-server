import scala.language.higherKinds

import cats.effect._
import io.circe.Json
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._

import scala.concurrent.ExecutionContext.Implicits.global

object Server extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    BlazeBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .mountService(routes, "/")
      .serve
      .compile
      .lastOrError
  }

  def routes[F[_]:Effect]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._
    HttpRoutes.of {
      case GET -> Root =>
        Ok(Json.True)
    }
  }
}

