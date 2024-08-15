package haxidenti.jmonna

import com.google.gson.Gson
import haxidenti.jmonna.service.User
import io.javalin.http.Context

val gson = Gson()

interface Endpoint {
    fun evaluate(c: Context, user: User?)
}
