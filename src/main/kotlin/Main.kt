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
            val xml: String? = updateCache(call.request.uri, yearMilli)
            if (xml != null) {
                try {
                    call.respond(xml.fromXml<CalendarYears>())
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            } else {
                call.respond(HttpStatusCode.NotFound)
            }

        }
        get("{year}/{term}/courses") {
            call.respond(getSemesterCourses())
        }
        get("/{year}") {
            val xml: String? = updateCache(call.request.uri, monthMilli)
            if (xml != null) {
                try {
                    call.respond(xml.fromXml<ScheduleYear>())
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            } else {
                call.respond(HttpStatusCode.NotFound)
            }

        }
        get("/{year}/{term}/") {
            val xml:String? = updateCache(call.request.uri, dayMilli)
            if (xml != null) {
                try {
                    call.respond(xml.fromXml<Term>())
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/{year}/{term}/{course}") {
            val xml:String? = updateCache(call.request.uri, hourMilli)
            if (xml != null) {
                try {
                    call.respond(xml.fromXml<Department>())
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/{year}/{term}/{course}/{code}") {
            val xml: String? = updateCache(call.request.uri, hourMilli)
            if (xml != null) {
                try {
                    call.respond(xml.fromXml<SubjectCourse>())
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/{year}/{term}/{course}/{code}/{section}") {
            val xml: String? = updateCache(call.request.uri, hourMilli)
            if (xml != null) {
                try {
                    call.respond(xml.fromXml<Section>())
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

suspend fun main(args: Array<String>) {
    loadSemesterCourses()
    embeddedServer(Netty, port = 8080, module = Application::run).start(wait = true)
}


