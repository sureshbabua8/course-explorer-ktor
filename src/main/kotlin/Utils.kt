package hello

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import java.io.File

private const val resDir: String = "src/main/cache/schedule"
private val client = HttpClient(CIO)
private var cacheMap: MutableMap<String, String> = HashMap() // maps path-dir to xmlString
private var timeStampMap: MutableMap<String, Long> = HashMap() // maps path-dir to timeStamp of XML object

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



