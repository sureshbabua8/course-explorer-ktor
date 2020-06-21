package hello

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import java.io.File
import java.lang.Exception


fun String.load() = Request::class.java.getResource("/$this").readText()

class Request {
    private val client = HttpClient(CIO)
    suspend fun getResponse(xmlString: String, type: String): Any {
        return when(type) {
            "ScheduleYear" -> {
                xmlString.fromXml<ScheduleYear>()
            }
            "CalendarYears" -> {
                xmlString.fromXml<CalendarYears>()
            }
            "Term" -> {
                xmlString.fromXml<Term>()
            }
            "Department" -> {
                xmlString.fromXml<Department>()
            }
            "SubjectCourse" -> {
                xmlString.fromXml<SubjectCourse>()
            }
            "Section" -> {
                xmlString.fromXml<Section>()
            }
            else -> {
                ""
            }
        }
    }
//    suspend fun getScheduleYear(path: String): ScheduleYear {
//        val xmlString = if (this.javaClass.getResource(path) != null) {
//            path.load()
//        } else {
//            client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/$path")
//        }
//    }

//    suspend suspend fun getCalendarYears(path: String): CalendarYears {
//
//    }
//
//    suspend fun getTerm(path: String): Term {
//
//    }
//
//    suspend fun getDepartment(path: String): Department {
//
//    }
//
//    suspend getSubjectCourse(path: String): SubjectCourse {
//
//    }
//
//    suspend getSection(path: String): Section {
//
//    }
}