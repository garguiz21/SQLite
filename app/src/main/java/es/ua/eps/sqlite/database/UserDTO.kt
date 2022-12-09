package es.ua.eps.sqlite.database

class UserDTO(val id: Int, val user_name: String, val full_name: String, val password: String, val email: String) {
    override fun toString(): String {
        return user_name
    }
}