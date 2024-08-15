package haxidenti.jmonna.service

import io.javalin.websocket.WsConfig
import io.javalin.websocket.WsContext

class WsBroadcaster {
    private val connections = mutableSetOf<WsContext>()

    fun serve(w: WsConfig) {
        w.onConnect { ws -> connections.add(ws) }
        w.onClose { ws -> connections.remove(ws) }
        w.onMessage { ws -> broadCast(ws, ws.message()) }

        // Print to console any errors
        w.onError { ws -> ws.error()?.printStackTrace() }
    }

    private fun broadCast(currentWs: WsContext, message: String) {
        connections.filter { it != currentWs }.forEach { it.send(message) }
    }
}