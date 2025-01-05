package example.web

import example.user.UserService
import zio.ZIO
import zio.http.endpoint.{AuthType, Endpoint}
import zio.http.{Handler, RoutePattern, Status, handler, string}
import zio.schema.{DeriveSchema, Schema}

object UserApi {
  case class GetUserResponse(id: String, name: String)
  object GetUserResponse {
    implicit val schema: Schema[GetUserResponse] = DeriveSchema.gen
  }

  case class NotFoundError(message: String)
  object NotFoundError {
    implicit val schema: Schema[NotFoundError] = DeriveSchema.gen
  }

  val endpoint: Endpoint[String, String, NotFoundError, GetUserResponse, AuthType.None] =
    Endpoint(RoutePattern.GET / "users" / string("id"))
      .out[GetUserResponse]
      .outError[NotFoundError](Status.NotFound)

  val getUserHandler: Handler[UserService, NotFoundError, String, GetUserResponse] =
    handler((id: String) => {
      ZIO.serviceWithZIO[UserService](userService =>
        userService.get(id)
          .map(userView => ZIO.succeed(GetUserResponse(id = userView.id, name = userView.name)))
          .getOrElse(ZIO.fail(NotFoundError(s"id($id) not found")))
      )
    })

  val routes = endpoint.implementHandler(getUserHandler).toRoutes
}
