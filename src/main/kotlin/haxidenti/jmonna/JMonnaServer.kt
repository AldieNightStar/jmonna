package haxidenti.jmonna

import haxidenti.jmonna.service.InMemoryUserStore
import haxidenti.jmonna.service.UserService
import haxidenti.jmonna.service.UserStore
import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.staticfiles.Location
import io.javalin.websocket.WsConfig
import java.io.File


class JMonnaServer(
    val staticFiles: File = File("./web"),
    val userStore: UserStore = InMemoryUserStore(),
) {
    private val javalin: Javalin = Javalin.create { config ->
        config.staticFiles.add(staticFiles.also { it.mkdirs() }.canonicalPath, Location.EXTERNAL)
    }

    private val userService = UserService(javalin, userStore)

    fun serve(port: Int): JMonnaServer {
        userService.serve()
        javalin.start(port)

        return this
    }

    fun get(path: String, endpoint: Endpoint, userRequired: Boolean = false): JMonnaServer {
        javalin.get(path) { handleEndpoint(it, endpoint, userRequired) }
        return this
    }

    fun post(path: String, endpoint: Endpoint, userRequired: Boolean = false): JMonnaServer {
        javalin.post(path) { handleEndpoint(it, endpoint, userRequired) }
        return this
    }

    fun put(path: String, endpoint: Endpoint, userRequired: Boolean = false): JMonnaServer {
        javalin.put(path) { handleEndpoint(it, endpoint, userRequired) }
        return this
    }

    fun delete(path: String, endpoint: Endpoint, userRequired: Boolean = false): JMonnaServer {
        javalin.delete(path) { handleEndpoint(it, endpoint, userRequired) }
        return this
    }

    fun head(path: String, endpoint: Endpoint, userRequired: Boolean = false): JMonnaServer {
        javalin.head(path) { handleEndpoint(it, endpoint, userRequired) }
        return this
    }

    fun options(path: String, endpoint: Endpoint, userRequired: Boolean = false): JMonnaServer {
        javalin.options(path) { handleEndpoint(it, endpoint, userRequired) }
        return this
    }

    fun ws(path: String, handler: (WsConfig) -> Unit): JMonnaServer {
        javalin.ws(path, handler)
        return this
    }


    private fun handleEndpoint(c: Context, endpoint: Endpoint, userRequired: Boolean) {
        val user = if (userRequired) userService.validateUserFromHeader(c) else null
        endpoint.evaluate(c, user)
    }


    fun javalin(): Javalin {
        return javalin
    }
}