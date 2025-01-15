package example.web

import example.local.InMemoryUserQuery
import example.user.UserService
import zio.ZLayer

type ServerEnvironment = UserApi

object ServerEnvironment {
  val live: ZLayer[InMemoryUserQuery.DataSource, Nothing, ServerEnvironment] =
    ZLayer.makeSome[InMemoryUserQuery.DataSource, ServerEnvironment](
      UserApi.live,
      // application
      UserService.live,
      // adapter/local
      InMemoryUserQuery.live
    )
}

