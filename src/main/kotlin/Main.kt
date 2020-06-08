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
import io.ktor.client.request.get
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.json.JSONObject
import org.json.XML

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
            call.respondText("Welcome to UIUC!") // will change to default show Fall 2020 courses
        }
        get("/{year}") {
            try {
                xml = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/" +
                        call.parameters["year"] + ".xml")
                val xmlJSONObj: JSONObject = XML.toJSONObject(xml)
                call.respondText(xmlJSONObj.toString(4))
            } catch (e: Exception) {
                call.respondText("Invalid Request!  Input a valid year.")
            }
        }
        get("/{year}/{term}/") {
            try {
                xml = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/" +
                        call.parameters["year"] + "/" + call.parameters["term"] + ".xml")
                val xmlJSONObj: JSONObject = XML.toJSONObject(xml)
                call.respondText(xmlJSONObj.toString(4))

            } catch (e: Exception) {
                call.respondText("Invalid Request!  Input a valid year and term.")
            }
        }

        get("/{year}/{term}/{course}") {
            xml = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/" +
                    call.parameters["year"] + "/" + call.parameters["term"] + "/" + call.parameters["course"] + ".xml")
            val xmlJSONObj: JSONObject = XML.toJSONObject(xml)
            call.respondText(xmlJSONObj.toString(4))

        }

        get("/{year}/{term}/{course}/{code}") {
            xml = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/" +
                    call.parameters["year"] + "/" + call.parameters["term"] + "/" + call.parameters["course"] + "/" +
                    call.parameters["code"] + ".xml")
            val xmlJSONObj: JSONObject = XML.toJSONObject(xml)
            println(xmlJSONObj)
            call.respondText(xmlJSONObj.toString(4))



        }
    }
}
 fun main() {
     embeddedServer(Netty, port = 8000, module = Application::viewCourse).start(wait = true)
 }
