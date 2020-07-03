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
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun Application.run() {
    val yearMilli: Long = 31540000000
    val monthMilli: Long = 2628000000
    val dayMilli: Long = 86400000
    val hourMilli: Long = 3600000

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
            try {
                call.respond(updateCache(call.request.uri, yearMilli).fromXml<CalendarYears>())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        get("/{year}") {
            try {
                call.respond(updateCache(call.request.uri, monthMilli).fromXml<ScheduleYear>())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest)
            }

        }
        get("/{year}/{term}/") {
            try {
                call.respond(updateCache(call.request.uri, dayMilli).fromXml<Term>())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/{year}/{term}/{course}") {
            try {
                call.respond(updateCache(call.request.uri, hourMilli).fromXml<Department>())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/{year}/{term}/{course}/{code}") {
            try {
                call.respond(updateCache(call.request.uri, hourMilli).fromXml<SubjectCourse>())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/{year}/{term}/{course}/{code}/{section}") {
            try {
                call.respond(updateCache(call.request.uri, hourMilli).fromXml<Section>())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, module = Application::run).start(wait = true)
}


