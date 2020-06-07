package hello

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


data class calendarYear(val label: String, val terms: List<term>, val id: String)
data class term(val id: String, val href: String, val label: String)


fun Application.viewCourse() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    routing {
        get("/") {
            val tester: String = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><calendarYear xmlns:ns2=\"http://rest.cis.illinois.edu\" id=\"2012\"><label>2012</label><terms><term id=\"120121\" href=\"https://courses.illinois.edu/cisapp/explorer/schedule/2012/spring.xml\"><label>Spring 2012</label></term><term id=\"120125\" href=\"https://courses.illinois.edu/cisapp/explorer/schedule/2012/summer.xml\"><label>Summer 2012</label></term><term id=\"120128\" href=\"https://courses.illinois.edu/cisapp/explorer/schedule/2012/fall.xml\"><label>Fall 2012</label></term></terms></calendarYear>"
            val xmlMapper: ObjectMapper = XmlMapper().registerKotlinModule()
            val value: calendarYear = xmlMapper.readValue(tester, calendarYear::class.java)
            val objectMapper = ObjectMapper()
            call.respondText(objectMapper.writeValueAsString(value))
        }
    }
}
suspend fun main() {
    val client = HttpClient(CIO)
    // Get the content of an URL.
    println(client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/2012.xml").replace("ns2:", ""))
    embeddedServer(Netty, port = 8000, module = Application::viewCourse).start(wait = true)
}
