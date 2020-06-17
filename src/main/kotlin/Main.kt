package hello

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
//import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
//import com.fasterxml.jackson.dataformat.xml.XmlMapper
//import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.ClientRequestException
import io.ktor.client.request.get
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.io.File

//private fun String.load() = Schedule::class.java.getResource("/$this").readText()

val client = HttpClient(CIO)
fun Application.viewCourse() {
    var xml = ""


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
            call.respond(File("schedule.xml").readText().fromXml<CalendarYears>())
        }
        get("/{year}") {
            try {
                xml = client.get("https://courses.illinois.edu/cisapp/explorer/schedule/" + call.parameters["year"] + ".xml")
                call.respond(xml.fromXml<ScheduleYear>())
            } catch (e: Exception) {
                call.respondText("Invalid Request!  Input a valid year.")
            }
        }
        get("/{year}/{term}/") {
            try {
                xml = client.get("https://courses.illinois.edu/cisapp/explorer/schedule/" +
                        call.parameters["year"] + "/" + call.parameters["term"] + ".xml")
                call.respond(xml.fromXml<Term>())
            } catch (e: Exception) {
                call.respondText("Invalid Request!  Input a valid year and term.")
            }
        }

        get("/{year}/{term}/{course}") {
            try {
                xml = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/" +
                        call.parameters["year"] + "/" + call.parameters["term"] + "/" + call.parameters["course"] + ".xml")
                call.respond(xml.fromXml<Department>())
            } catch (e: Exception) {
                call.respondText("Invalid Request!  Input a valid year, term, and subject code.")
            }

        }

        get("/{year}/{term}/{course}/{code}") {
            try {
                xml = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/" +
                        call.parameters["year"] + "/" + call.parameters["term"] + "/" + call.parameters["course"] + "/" + call.parameters["code"] + ".xml")
                call.respond(xml.fromXml<SubjectCourse>())
            } catch (e: Exception) {
                call.respondText("Invalid Request!  Input a valid year, term, subject, and course code.")
            }
        }
        get("/{year}/{term}/{course}/{code}/{section}") {
            try {
                xml = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/" +
                        call.parameters["year"] + "/" + call.parameters["term"] + "/" + call.parameters["course"] + "/" +
                        call.parameters["code"] + "/" + call.parameters["section"] + ".xml")
                call.respond(xml.fromXml<Section>())
            } catch (e: Exception) {
                call.respondText("Invalid Request!  Input a valid year, term, subject, course code, " +
                        "and CRN for the specific section.")
            }
        }
    }
}

 suspend fun main() {
     val path = "https://courses.illinois.edu/cisapp/explorer/schedule.xml"
     val fileContent: String = client.get(path)
     val file = File("schedule.xml")
     file.writeText(fileContent)
     embeddedServer(Netty, port = 8080, module = Application::viewCourse).start(wait = true)
 }
