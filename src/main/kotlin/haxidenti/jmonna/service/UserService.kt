package haxidenti.jmonna.service

import com.google.gson.Gson
import haxidenti.jmonna.dto.LoginDto
import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.HttpResponseException
import java.util.*

private val gson = Gson()

internal class UserService(
    val javalin: Javalin,
    val store: UserStore
) {
    private val userTokens = mutableMapOf<UUID, String>()

    fun serve() {
        serveLoginRegister()
    }

    fun generateToken(login: String): UUID {
        val token = UUID.randomUUID()
        userTokens[token] = login
        return token
    }

    fun parseToken(token: UUID): User? {
        val login = userTokens[token] ?: return null
        return store.getUser(login)
    }

    fun validateUserFromHeader(c: Context): User {
        val token = c.header("Token") ?: throw HttpResponseException(403, "Bad Token")
        val uuid = try {
            UUID.fromString(token)
        } catch (e: IllegalArgumentException) {
            throw HttpResponseException(403, "Bad Token")
        }
        return parseToken(uuid) ?: throw HttpResponseException(403, "Bad Token")
    }

    private fun serveLoginRegister() {
        javalin.post("/auth/login") { c ->
            val dto = gson.fromJson(c.body(), LoginDto::class.java)
            val user = store.getUser(dto.login) ?: throw HttpResponseException(401, "Wrong Credentials")
            if (user.password != dto.password) throw HttpResponseException(401, "Wrong Credentials")
            c.result(generateToken(dto.login).toString())
        }
        javalin.post("/auth/register") { c ->
            val dto = gson.fromJson(c.body(), LoginDto::class.java)
            if (store.getUser(dto.login) != null) throw HttpResponseException(401, "Such user is already exist")
            store.setUser(dto.login, dto.password)
            c.result(generateToken(dto.login).toString())
        }
    }
}