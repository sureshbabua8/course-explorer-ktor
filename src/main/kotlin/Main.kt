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

private val client = HttpClient(CIO)
private val resDir: String = "src/main/cache/schedule"

private suspend fun updateCache(originalUri: String, timeLapse: Long): String {
    val newUri = if (originalUri == "/") {
        originalUri.trim('/')
    } else {
        originalUri
    }
    val path: String = resDir + originalUri.replace('/', '-')
    val xmlString: String
    val file: File = File("$path.xml")
    if (file.exists() && file.lastModified() < timeLapse) {
        xmlString = file.readText()
    } else {
        xmlString = client.get("https://courses.illinois.edu/cisapp/explorer/schedule$newUri.xml")
        File("$path.xml").writeText(xmlString)
    }

    return xmlString
}

fun Application.run() {
    val year: Long = 31540000000
    val month: Long = 2628000000
    val day: Long = 86400000
    val hour: Long = 3600000

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
            call.respond(updateCache(call.request.uri, year).fromXml<CalendarYears>())
        }
        get("/{year}") {
            call.respond(updateCache(call.request.uri, month).fromXml<ScheduleYear>())

        }
        get("/{year}/{term}/") {
            call.respond(updateCache(call.request.uri, day).fromXml<Term>())
        }

        get("/{year}/{term}/{course}") {
            call.respond(updateCache(call.request.uri, hour).fromXml<Department>())
        }

        get("/{year}/{term}/{course}/{code}") {
            call.respond(updateCache(call.request.uri, hour).fromXml<SubjectCourse>())
        }
        get("/{year}/{term}/{course}/{code}/{section}") {
            call.respond(updateCache(call.request.uri, hour).fromXml<Section>())
        }


    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, module = Application::run).start(wait = true)
}


