package hello

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.google.gson.Gson
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

data class Schedule(val id: String)

fun Application.viewCourse() {
    install(ContentNegotiation) {
        gson {

        }
    }

    routing {
        get("/") {
            val client = HttpClient(CIO)
            // Get the content of an URL.
            val xmlMapper = XmlMapper().registerKotlinModule()

            val xml = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/2012.xml")
            call.respond(xmlMapper.readValue<Schedule>(xml))
        }
    }
}
suspend fun main() {
    embeddedServer(Netty, port = 8000, module = Application::viewCourse).start(wait = true)
}
