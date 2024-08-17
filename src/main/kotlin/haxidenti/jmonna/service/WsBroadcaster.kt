package haxidenti.jmonna.service

import io.javalin.websocket.WsConfig
import io.javalin.websocket.WsContext
import java.time.Duration

class WsBroadcaster {
    private val connections = mutableListOf<WsContext>()

    fun serve(w: WsConfig) {
        w.onConnect { ws ->
            ws.session.policy.idleTimeout = Duration.ofHours(1)
            connections.add(ws)
        }
        w.onClose { ws -> connections.remove(ws) }
        w.onMessage { ws ->
            broadCast(ws, ws.message())
        }

        // Print to console any errors
        w.onError { ws -> ws.error()?.printStackTrace() }
    }

    private fun broadCast(currentWs: WsContext, message: String) {
        connections.filter { it != currentWs }.forEach { it.send(message) }
    }
}