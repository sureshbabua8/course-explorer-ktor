@file:Suppress("SpellCheckingInspection")

package hello
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.response.respondBytes
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.util.logging.XMLFormatter
import javax.xml.crypto.dsig.XMLObject

suspend fun foo(): ByteArray {
    val client = HttpClient(CIO)

    // Get the content of an URL.
    return client.get<ByteArray>("https://courses.illinois.edu/cisapp/explorer/schedule/2012.xml")
}

fun Application.viewCourse() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    routing {
        get("/") {
            call.respondBytes(foo())
        }
    }
}
suspend fun main() {
    embeddedServer(Netty, port = 8000, module = Application::viewCourse).start(wait = true)
}
