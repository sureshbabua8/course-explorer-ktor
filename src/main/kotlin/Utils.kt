package hello

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import java.io.File

private const val resDir: String = "src/main/cache/schedule"
private val client = HttpClient(CIO)
private var cacheMap: MutableMap<String, String> = HashMap() // maps path-dir to xmlString
private var timeStampMap: MutableMap<String, Long> = HashMap() // maps path-dir to timeStamp of XML object
private val courses = mutableListOf<Department.Course>()


suspend fun updateCache(originalUri: String, timeLapse: Long): String? {
    val path: String = resDir + originalUri
    val newUri = if (originalUri == "/") {
        originalUri.trim('/')
    } else {
        originalUri
    }

    if (cacheMap.containsKey(path) && System.currentTimeMillis() - timeStampMap[path]!! < timeLapse) {
        return cacheMap[path]
    } else {
        try {
            val xmlString: String = client.get("https://courses.illinois.edu/cisapp/explorer/schedule$newUri.xml")
            cacheMap[path] = xmlString
            timeStampMap[path] = System.currentTimeMillis()
        } catch (e: Exception) {
            return null
        }
    }

    return cacheMap[path]
}

suspend fun loadSemesterCourses(): Unit {
    // load all departments' courses
    // return list
    // go through all departments for the Fall 2020 semester
    val semester: Term = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall.xml").fromXml()
    for (subject in semester.subjects) {
        // get ID from subject
        val id = subject.id
        // load all departments' courses
        val dept: Department = client.get<String>(subject.href.toURL()).fromXml()
        // add to list of courses
        courses.addAll(dept.courses)
    }
}

fun getSemesterCourses(): List<Department.Course> {
    return courses
}



