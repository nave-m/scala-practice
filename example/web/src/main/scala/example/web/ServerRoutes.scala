package example.web

import zio.{ZIO, ZLayer}
import zio.http.Status
import zio.http.{Body, Response, Routes}
import zio.json.ast.Json
import zio.schema.codec.JsonCodec._

object ServerRoutes {
  val routes: Routes[UserApi, Response] = UserEndpoints.routes.handleErrorCauseZIO {cause => 
    cause.failureOrCause match {
      case Left(e: ServiceError.NotFound) => 
        ZIO.succeed {
          Response(
            status = Status.NotFound,
            body = Body.from(Json("message" -> Json.Str(e.getMessage)))
          )
        }
      case Right(cause) => 
        for {
          _ <- ZIO.logErrorCause("Unexpected", cause)
        } yield {
          Response.status(Status.InternalServerError)
        }
    }
  }
  val live: ZLayer[UserApi, Nothing, Routes[Any, Response]] = ZLayer {
    for {
      any <- ZIO.environment[UserApi]
    } yield {
      routes.provideEnvironment(any)
    }
  }
}
