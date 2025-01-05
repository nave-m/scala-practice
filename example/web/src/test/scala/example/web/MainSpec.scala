package example.web

import example.local.InMemoryUserQuery
import example.user.{UserService, UserView}
import zio._
import zio.test._
import zio.http._

class MainSpec extends ZIOSpec[Server & Client & TestEnvironment] {
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
  }.provide(
    TestServer.layer,
    Client.default,
    ZLayer.succeed(
      UserService(
        query = InMemoryUserQuery(
          users = Array(
            UserView(id = "1", name = "foo"),
            UserView(id = "2", name = "bar"),
          )
        )
      )
    ),
  )

  override def bootstrap: ZLayer[Any, Any, Server & Client & TestEnvironment] =
    ZLayer.make[Server & Client & TestEnvironment](
      testEnvironment,
      httpServer,
      httpClient,
    )

  private lazy val httpServer: ZLayer[Any, Nothing, Server] = {
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
        fiber  <- Server.serve(UserApi.routes).forkIn(scope)
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
