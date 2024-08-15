package haxidenti.jmonna

import haxidenti.jmonna.service.*
import haxidenti.jmonna.service.UserService
import haxidenti.jmonna.service.WsBroadcaster
import haxidenti.jmonna.util.Request
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

    fun broadcast(path: String): JMonnaServer {
        javalin.ws(path) { WsBroadcaster().serve(it) }
        return this
    }

    fun get(path: String, endpoint: Endpoint): JMonnaServer {
        javalin.get(path) { handleEndpoint(it, endpoint) }
        return this
    }

    fun post(path: String, endpoint: Endpoint): JMonnaServer {
        javalin.post(path) { handleEndpoint(it, endpoint) }
        return this
    }

    fun put(path: String, endpoint: Endpoint): JMonnaServer {
        javalin.put(path) { handleEndpoint(it, endpoint) }
        return this
    }

    fun delete(path: String, endpoint: Endpoint): JMonnaServer {
        javalin.delete(path) { handleEndpoint(it, endpoint) }
        return this
    }

    fun head(path: String, endpoint: Endpoint): JMonnaServer {
        javalin.head(path) { handleEndpoint(it, endpoint) }
        return this
    }

    fun options(path: String, endpoint: Endpoint): JMonnaServer {
        javalin.options(path) { handleEndpoint(it, endpoint) }
        return this
    }

    fun ws(path: String, handler: (WsConfig) -> Unit): JMonnaServer {
        javalin.ws(path, handler)
        return this
    }

    private fun handleEndpoint(c: Context, endpoint: Endpoint) {
        val request = Request(
            context = c,
            userGetter = {
                userService.validateUserFromHeader(c)
            }
        )
        val result = endpoint(request)
        if (result != null) {
            c.result(gson.toJson(result))
        }
    }


    fun javalin(): Javalin {
        return javalin
    }
}