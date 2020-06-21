package hello

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.io.File

fun Application.run() {
    val client = HttpClient(CIO)
    val resDir: String = "src/main/resources/schedule"

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
            val uri: String = call.request.uri.trim('/')
            val path: String = resDir + call.request.uri.replace('/', '-')
            val xmlString: String
            val file: File = File("$path.xml")
            if (file.exists()) {
                xmlString = file.readText()
            } else {
                xmlString = client.get("https://courses.illinois.edu/cisapp/explorer/schedule$uri.xml")
                File("$path.xml").writeText(xmlString)
            }

            call.respond(xmlString.fromXml<CalendarYears>())
        }
        get("/{year}") {
            val uri: String = call.request.uri
            val path: String = resDir + call.request.uri.replace('/', '-')
            val xmlString: String
            val file: File = File("$path.xml")
            if (file.exists()) {
                xmlString = file.readText()
            } else {
                xmlString = client.get("https://courses.illinois.edu/cisapp/explorer/schedule$uri.xml")
                File("$path.xml").writeText(xmlString)
            }

            call.respond(xmlString.fromXml<ScheduleYear>())

        }
        get("/{year}/{term}/") {
            val uri: String = call.request.uri
            val path: String = resDir + call.request.uri.replace('/', '-')
            val xmlString: String
            val file: File = File("$path.xml")
            if (file.exists()) {
                xmlString = file.readText()
            } else {
                xmlString = client.get("https://courses.illinois.edu/cisapp/explorer/schedule$uri.xml")
                File("$path.xml").writeText(xmlString)
            }

            call.respond(xmlString.fromXml<Term>())
        }

        get("/{year}/{term}/{course}") {
            val uri: String = call.request.uri
            val path: String = resDir + call.request.uri.replace('/', '-')
            val xmlString: String
            val file: File = File("$path.xml")
            if (file.exists()) {
                xmlString = file.readText()
            } else {
                xmlString = client.get("https://courses.illinois.edu/cisapp/explorer/schedule$uri.xml")
                File("$path.xml").writeText(xmlString)
            }

            call.respond(xmlString.fromXml<Department>())
        }

        get("/{year}/{term}/{course}/{code}") {
            val uri: String = call.request.uri
            val path: String = resDir + call.request.uri.replace('/', '-')
            val xmlString: String
            val file: File = File("$path.xml")
            if (file.exists()) {
                xmlString = file.readText()
            } else {
                xmlString = client.get("https://courses.illinois.edu/cisapp/explorer/schedule$uri.xml")
                File("$path.xml").writeText(xmlString)
            }

            call.respond(xmlString.fromXml<SubjectCourse>())
        }
        get("/{year}/{term}/{course}/{code}/{section}") {
            val uri: String = call.request.uri
            val path: String = resDir + call.request.uri.replace('/', '-')
            val xmlString: String
            val file: File = File("$path.xml")
            if (file.exists()) {
                xmlString = file.readText()
            } else {
                xmlString = client.get("https://courses.illinois.edu/cisapp/explorer/schedule$uri.xml")
                File("$path.xml").writeText(xmlString)
            }

            call.respond(xmlString.fromXml<Section>())
        }
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, module = Application::run).start(wait = true)
}


