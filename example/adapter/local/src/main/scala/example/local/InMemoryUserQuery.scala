package example.local

import example.user.{UserQuery, UserView}
import zio.{ZIO, ZLayer}

class InMemoryUserQuery(
  private val users: Array[UserView]
) extends UserQuery {
  override def findById(id: String): ZIO[Any, Nothing, Option[UserView]] =
    ZIO.succeed(users.find(view => view.id == id))
}

object InMemoryUserQuery {
  def apply(users: Array[UserView]) = new InMemoryUserQuery(users)
  case class DataSource(users: Array[UserView])
  val live: ZLayer[InMemoryUserQuery.DataSource, Nothing, UserQuery] = ZLayer {
    for {
      dataSource <- ZIO.service[InMemoryUserQuery.DataSource]
    } yield new InMemoryUserQuery(users = dataSource.users)
  }
}
