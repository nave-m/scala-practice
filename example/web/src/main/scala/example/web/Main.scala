package example.web

import zio._
import zio.http._
import example.user.UserView
import example.local.InMemoryUserQuery

object Main extends ZIOAppDefault {
  private lazy val userDataSource: ZLayer[Any, Nothing, InMemoryUserQuery.DataSource] = {
    ZLayer.succeed(
      InMemoryUserQuery.DataSource(
        users = Array(
          UserView(id = "1", name = "foo"),
          UserView(id = "2", name = "bar"),
        )
      )
    )
  }

  def run =
    Server.serve(ServerRoutes.routes)
      .provide(
        Server.default,
        ServerEnvironment.live,
        userDataSource
      )
}
