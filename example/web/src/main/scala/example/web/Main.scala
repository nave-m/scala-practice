package example.web

import zio._
import zio.http._
import example.user.{UserService, UserView}
import example.local.InMemoryUserQuery

object Main extends ZIOAppDefault {
  def run=
    Server.serve(UserApi.routes)
      .provide(
        Server.default,
        ZLayer.succeed(
          UserService(
            query = InMemoryUserQuery(
              users = Array(
                UserView(id = "1", name = "foo"),
                UserView(id = "2", name = "bar"),
              )
            )
        )
      )
    )
}
