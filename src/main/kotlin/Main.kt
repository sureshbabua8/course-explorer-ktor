package hello

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

val client = HttpClient(CIO)
fun Application.viewCourse() {
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
            setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
                indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
                indentObjectsWith(DefaultIndenter("  ", "\n"))
            })
        }
    }

    routing {
        get("/") {
            call.respondText("Welcome to UIUC!") // will change to default show Fall 2020 courses
        }
        get("/{year}") {
            try {
                var xml: String = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/"+call.parameters["year"] + ".xml")
                xml = xml.replace(".xml\">", ".xml\"><label>")
                xml = xml.replace("</term>", "</label></term>")
                val xmlMapper: ObjectMapper = XmlMapper().registerKotlinModule()
                val value: calendarYear = xmlMapper.readValue(xml, calendarYear::class.java)
                val objectMapper = ObjectMapper()
                call.respondText(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value))
            } catch (e: Exception) {
                call.respondText("Ope!")
            }
        }
        get("/{year}/{term}/") {
            call.respondText("will update later")
        }
    }
}
 suspend fun main() {
     var xml: String = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/2020.xml")
     xml = xml.replace(".xml\">", ".xml\"><label>")
     xml = xml.replace("</term>", "</label></term>")
     val xmlMapper: ObjectMapper = XmlMapper().registerKotlinModule()
     val value: calendarYear = xmlMapper.readValue(xml, calendarYear::class.java)
     val objectMapper = ObjectMapper()
     println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value))
    embeddedServer(Netty, port = 8000, module = Application::viewCourse).start(wait = true)
}
