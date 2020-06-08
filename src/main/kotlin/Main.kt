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
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.json.JSONArray
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
                xml = client.get("https://courses.illinois.edu/cisapp/explorer/schedule/" + call.parameters["year"] + ".xml")
                val jsonObj: JSONObject = XML.toJSONObject(xml)
                jsonObj.put("calendarYear", jsonObj.remove("ns2:calendarYear"))
                val calendar = jsonObj.getJSONObject("calendarYear")
                calendar.remove("label")
                calendar.remove("xmlns:ns2")
                calendar.put("year", calendar.remove("id"))
                calendar.put("term", calendar.getJSONObject("terms").getJSONArray("term"))
                calendar.remove("terms")
                for (item in calendar.getJSONArray("term")) {
                    (item as JSONObject).remove("id")
                }
                call.respondText(jsonObj.toString(4).replace("ns2:calendarYear", "calendarYear")
                    .replace("term", "terms"))
            } catch (e: Exception) {
                call.respondText("Invalid Request!  Input a valid year.")
            }
        }
        get("/{year}/{term}/") {
            try {
                xml = client.get("https://courses.illinois.edu/cisapp/explorer/schedule/" +
                        call.parameters["year"] + "/" + call.parameters["term"] + ".xml")
                val jsonObj: JSONObject = XML.toJSONObject(xml)
                jsonObj.put("term", jsonObj.remove("ns2:term"))
                val term = jsonObj.getJSONObject("term")
                term.remove("xmlns:ns2")
                term.remove("parents")
                term.remove("id")
                term.put("subject", term.getJSONObject("subjects").getJSONArray("subject"))
                term.remove("subjects")
                for (item in term.getJSONArray("subject")) {
                    (item as JSONObject).put("subjectName", item.remove("content"))
                    item.put("code", item.remove("id"))
                }
                term.put("subjects", term.remove("subject"))
                call.respondText(jsonObj.toString(4))
            } catch (e: Exception) {
                call.respondText("Invalid Request!  Input a valid year and term.")
            }
        }

        get("/{year}/{term}/{course}") {
            xml = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/" +
                    call.parameters["year"] + "/" + call.parameters["term"] + "/" + call.parameters["course"] + ".xml")
            val jsonObj: JSONObject = XML.toJSONObject(xml)
            jsonObj.put("subject", jsonObj.remove("ns2:subject"))
            var subject = jsonObj.getJSONObject("subject")
            subject.remove("label")
            subject.remove("contactName")
            subject.remove("departmentCode")
            subject.remove("collegeCode")
            subject.remove("phoneNumber")
            subject.remove("xmlns:ns2")
            subject.remove("addressLine1")
            subject.remove("addressLine2")
            subject.remove("contactTitle")
            subject.remove("collegeDepartmentDescription")
            subject.put("subjectId", subject.remove("id"))
            subject.put("term", subject.getJSONObject("parents").getJSONObject("term").get("content"))
            subject.remove("parents")
            subject.put("course", subject.getJSONObject("courses").get("course"))
            subject.remove("courses")
            if (subject.toString().contains("\"course\":{")) {
                val course = subject.get("course")
                subject.put("courses", JSONArray().put(course))
                subject.remove("course")
            } else {
                subject.put("courses", subject.remove("course"))
            }

            for (course in subject.getJSONArray("courses")) {
                (course as JSONObject).put("courseTitle", course.remove("content"))
                course.put("courseId", course.remove("id"))
            }
            call.respondText(jsonObj.toString(4))

        }

        get("/{year}/{term}/{course}/{code}") {
            xml = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/" +
                    call.parameters["year"] + "/" + call.parameters["term"] + "/" + call.parameters["course"] + "/" + call.parameters["code"] + ".xml")
            val jsonObj: JSONObject = XML.toJSONObject(xml)
            jsonObj.put("course", jsonObj.remove("ns2:course"))
            val course = jsonObj.getJSONObject("course")
            course.remove("xmlns:ns2")
            course.remove("href")
            if (course.has("genEdCategories")) {
                val genEd = course.getJSONObject("genEdCategories").get("category")
                if (genEd.toString().startsWith("{\"description")) {
                    course.put("genEd", JSONArray().put(genEd))
                } else {
                    course.put("genEd", genEd)
                }
                course.remove("genEdCategories")
                for (courseElem in course.getJSONArray("genEd")) {
                    val req = courseElem as JSONObject
                    req.put("description", req.getJSONObject("ns2:genEdAttributes").getJSONObject("genEdAttribute").get("content"))
                    req.put("id", req.getJSONObject("ns2:genEdAttributes").getJSONObject("genEdAttribute").get("code"))
                    req.remove("ns2:genEdAttributes")
                }
            }
            // reconfig course sections JSON data
            course.put("section", course.getJSONObject("sections").getJSONArray("section"))
            course.remove("sections")
            course.put("courseTitle", course.remove("label"))
            course.put("courseId", course.remove("id"))
            course.put("term", course.getJSONObject("parents").getJSONObject("term").get("content"))
            course.remove("parents")
            call.respondText(jsonObj.toString(4).replace("section", "sections"))

        }
        get("/{year}/{term}/{course}/{code}/{section}") {
            xml = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/" +
                    call.parameters["year"] + "/" + call.parameters["term"] + "/" + call.parameters["course"] + "/" +
                    call.parameters["code"] + "/" + call.parameters["section"] + ".xml")

            val jsonObj: JSONObject = XML.toJSONObject(xml)
            jsonObj.put("section", jsonObj.remove("ns2:section"))
            val section = jsonObj.getJSONObject("section")
            section.remove("xmlns:ns2")
            section.remove("href")
            section.remove("statusCode")
            section.put("crn", section.remove("id"))
            section.put("meeting", section.getJSONObject("meetings").getJSONObject("meeting"))
            section.remove("meetings")
            section.put("year", section.getJSONObject("parents").getJSONObject("calendarYear").get("content"))
            section.put("term", section.getJSONObject("parents").getJSONObject("term").get("content"))
            section.put("course", section.getJSONObject("parents").getJSONObject("subject").get("id"))
            section.put("department", section.getJSONObject("parents").getJSONObject("subject").get("content"))
            section.put("courseTitle", section.getJSONObject("parents").getJSONObject("course").get("content"))
            section.remove("parents")

            val meeting = section.getJSONObject("meeting")
            meeting.put("instructor", meeting.getJSONObject("instructors").get("instructor"))
            meeting.remove("instructors")
            if (meeting.toString().contains("\"instructor\":{")) {
                val instructor = meeting.get("instructor")
                meeting.put("instructors", JSONArray().put(instructor))
                meeting.remove("instructor")
            }
            call.respondText(jsonObj.toString(4))
        }
    }
}
 suspend fun main() {
//     var xml = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall/CS/125/66448.xml")
     embeddedServer(Netty, port = 8000, module = Application::viewCourse).start(wait = true)
 }
