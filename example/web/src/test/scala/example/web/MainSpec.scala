package example.web

import example.local.InMemoryUserQuery
import example.user.UserView
import zio._
import zio.test._
import zio.http._

class MainSpec extends ZIOSpec[ServerTestEnvironment & TestEnvironment] {
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

  def spec = suiteAll("GET users/{id}") {
    test("存在するIDを指定すると200応答でボディはJSON形式") {
      for {
        client <- ZIO.service[Client]
        response <- client.get("/users/1")
        body <- response.body.asString
      } yield {
        assertTrue(
          response.status == Status.Ok,
          body == "{\"id\":\"1\",\"name\":\"foo\"}"
        )
      }
    }
    test("存在しないIDを指定すると404応答") {
      for {
        client <- ZIO.service[Client]
        response <- client.get("/users/1")
      } yield {
        assertTrue(
          response.status == Status.NotFound
        )
      }
    }
  }

  override def bootstrap: ZLayer[Any, Nothing, ServerTestEnvironment & TestEnvironment] =
    ZLayer.make[ServerTestEnvironment & TestEnvironment](
      testEnvironment,
      ServerEnvironment.live,
      userDataSource,
      MainSpec.httpServer,
      MainSpec.httpClient.orDie,
    )
}

object MainSpec {
  private lazy val httpServer: ZLayer[UserApi, Nothing, Server] = {
    val server = for {
      port   <- ZLayer { ZIO.randomWith(_.nextIntBetween(49152, 65535)) }
      server <- Server.defaultWithPort(port.get[Int])
    } yield {
      server
    }

    server >+> ZLayer.scoped {
      for {
        scope  <- ZIO.scope
        port   <- ZIO.randomWith(_.nextIntBetween(49152, 65535))
        _  <- Server.serve(ServerRoutes.routes).forkIn(scope)
        server <- ZIO.service[Server]
      } yield {
        server
      }
    }
  }.orDie

  private def httpClient: ZLayer[Any & Server, Throwable, Client] =
    Client.default >+> ZLayer {
      for {
        server <- ZIO.service[Server]
        client <- ZIO.service[Client]
        port   <- server.port
      } yield {
        client.host("127.0.0.1").port(port)
      }
    }
}
