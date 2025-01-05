package example.user

trait UserQuery {
  def findById(id: String): Option[UserView]
}
