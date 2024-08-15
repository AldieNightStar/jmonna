package haxidenti.jmonna.service

import java.util.UUID

interface UserStore {
    fun getUser(login: String): User?
    fun setUser(login: String, password: String): Boolean
}

class InMemoryUserStore : UserStore {
    private val map = mutableMapOf<String, User>()

    override fun getUser(login: String): User? {
        return map[login]
    }

    override fun setUser(login: String, password: String): Boolean {
        map[login] = User(login, password)
        return true
    }
}

data class User(
    val login: String,
    val password: String,
    val id: UUID = UUID.randomUUID()
)