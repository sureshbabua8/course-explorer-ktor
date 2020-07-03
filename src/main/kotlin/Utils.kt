package hello

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import java.io.File

private const val resDir: String = "src/main/cache/schedule"
private val client = HttpClient(CIO)
private var cacheMap: MutableMap<String, String> = HashMap() // maps path-dir to xmlString
private var timeStampMap: MutableMap<String, Long> = HashMap() // maps path-dir to timeStamp of XML object

suspend fun updateCache(originalUri: String, timeLapse: Long): String {
    val newUri = if (originalUri == "/") {
        originalUri.trim('/')
    } else {
        originalUri
    }

    val path: String = resDir + originalUri.replace('/', '-')
    val xmlString: String
    val file: File = File("$path.xml")

//    if (file.exists() && file.lastModified() < timeLapse) {
//        xmlString = file.readText()
//    } else {
//        xmlString = client.get("https://courses.illinois.edu/cisapp/explorer/schedule$newUri.xml")
//        File("$path.xml").writeText(xmlString)
//    }

    return xmlString
}



