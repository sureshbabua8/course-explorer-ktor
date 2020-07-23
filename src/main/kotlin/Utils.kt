package hello

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get

private const val resDir: String = "src/main/cache/schedule"
private val client = HttpClient(CIO)
private var cacheMap: MutableMap<String, String> = HashMap() // maps path-dir to xmlString
private var timeStampMap: MutableMap<String, Long> = HashMap() // maps path-dir to timeStamp of XML object
private var semesterCourses: MutableMap<String, List<Department.Course>> = HashMap()

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

suspend fun loadSemesterCourses(year: String, term: String) {
    // load all departments' courses
    // return list
    // go through all departments for the Fall 2020 semester
    val courses = mutableListOf<Department.Course>()
    val semester: Term
    try {
        semester = client.get<String>("https://courses.illinois.edu/cisapp/explorer/schedule/$year/$term.xml").fromXml()
    } catch (e: Exception) {
        return
    }
    for (subject in semester.subjects) {
        // get ID from subject
        val id = subject.id
        // load all departments' courses
        val dept: Department = client.get<String>(subject.href.toURL()).fromXml()
        // add to list of courses
        courses.addAll(dept.courses)
    }
    semesterCourses["/$year/$term"] = courses
}

suspend fun getSemesterCourses(year: String, term: String): List<Department.Course>? {
    if (semesterCourses.containsKey("/$year/$term")) {
        return semesterCourses["/$year/$term"]
    } else {
        loadSemesterCourses(year, term)
        return semesterCourses["/$year/$term"]
    }
}
