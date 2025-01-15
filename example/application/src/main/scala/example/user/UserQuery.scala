package example.user

import zio.UIO

trait UserQuery {
  def findById(id: String): UIO[Option[UserView]]
}
