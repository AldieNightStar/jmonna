package haxidenti.jmonna.util

import haxidenti.jmonna.gson
import haxidenti.jmonna.service.User
import io.javalin.http.Context

class Request(
    val context: Context,
    private val userGetter: () -> User?,
) {
    inline fun <reified T> data(): T {
        return gson.fromJson(context.body(), T::class.java)
    }

    fun user() = userGetter()
}