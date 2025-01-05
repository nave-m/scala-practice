package example.local

import example.user.{UserQuery, UserView}

class InMemoryUserQuery(
  private val users: Array[UserView]
) extends UserQuery {
  override def findById(id: String): Option[UserView] =
    users.find(view => view.id == id)
}

object InMemoryUserQuery {
  def apply(users: Array[UserView]) = new InMemoryUserQuery(users)
}
