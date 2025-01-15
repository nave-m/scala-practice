package example.web

import zio.http.Body
import zio.json.JsonEncoder

trait Endpoints {
  extension [A](a: A) {
    def asJsonBody(using JsonEncoder[A]): Body = Body.fromCharSequence(JsonEncoder[A].encodeJson(a, None))
  }
}
