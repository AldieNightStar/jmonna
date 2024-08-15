package haxidenti.jmonna;

import java.io.File

fun main() {
    JMonnaServer(
        staticFiles = File("./web"),
    ).serve(8080)
}