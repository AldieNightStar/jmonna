package haxidenti.jmonna

import com.google.gson.Gson
import haxidenti.jmonna.util.Request

val gson = Gson()

typealias Endpoint = (r: Request) -> Any?