package example.user

import zio.IO
import zio.ZLayer

class UserService(query: UserQuery) {
  def get(id: String): IO[UserService.GetError, UserView] = {
    query.findById(id)
      .someOrFail(UserService.UserNotFound.fromId(id))
  }
}

object UserService {
  type GetError = UserNotFound

  class UserNotFound(message: String) extends Exception(message)
  object UserNotFound {
    def fromId(id: String): UserNotFound = new UserNotFound(s"User not found: $id")
  }
  def apply(query: UserQuery) = new UserService(query)
  val live: ZLayer[UserQuery, Nothing, UserService] = ZLayer.derive[UserService]
}
