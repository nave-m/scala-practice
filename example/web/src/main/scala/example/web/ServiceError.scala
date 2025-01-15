package example.web

sealed abstract class ServiceError(message: String) extends Exception(message)

object ServiceError {
  class NotFound(message: String) extends ServiceError(message)
}
