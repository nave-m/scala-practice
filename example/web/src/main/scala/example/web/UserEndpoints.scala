package example.web


import zio.http.*
import zio.json.JsonEncoder

object UserEndpoints extends Endpoints {
  val routes: Routes[UserApi, ServiceError] = Routes(
    Method.GET / "users" / string("id") -> handler { (id: String, _: Request) =>
      for {
        response <- UserApi.get(id)
      } yield {
        Response(
          status = Status.Ok,
          body = response.asJsonBody
        )
      }
    }
  )
}
