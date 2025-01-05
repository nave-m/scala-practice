package example.user

class UserService(query: UserQuery) {
  def get(id: String): Option[UserView] = query.findById(id)
}

object UserService {
  def apply(query: UserQuery) = new UserService(query)
}
