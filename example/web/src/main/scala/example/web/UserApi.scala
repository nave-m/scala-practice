package example.web

import example.user.UserService
import zio.IO
import zio.ZIO
import zio.ZLayer
import zio.json.{DeriveJsonCodec, JsonCodec, SnakeCase, jsonMemberNames}
import zio.schema.{DeriveSchema, Schema}

class UserApi(
  userService: UserService
) {
  def get(id: String): IO[ServiceError, UserApi.GetUserResponse] = {
    for {
      user <- userService.get(id).mapError { case _: UserService.UserNotFound => new ServiceError.NotFound("User not found")}
    } yield {
      UserApi.GetUserResponse(id = user.id, name = user.name)
    }
  }
}

object UserApi {
  @jsonMemberNames(SnakeCase)
  case class GetUserResponse(id: String, name: String)
  object GetUserResponse {
    given JsonCodec[GetUserResponse] = DeriveJsonCodec.gen[GetUserResponse]
  }

  def get(id: String): ZIO[UserApi, ServiceError, UserApi.GetUserResponse] = ZIO.serviceWithZIO(_.get(id))
  val live: ZLayer[UserService, Nothing, UserApi] = ZLayer.derive[UserApi]
}