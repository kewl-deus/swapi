package swapi.logic

import java.io.InputStream
import java.net.URL

object SwapiResources {

    fun getResourceAsStream(resourceName: String): InputStream {
        val cls = SwapiResources::class.java
        return cls.getResourceAsStream("/swapi/data/$resourceName.json")
    }

    fun getResourceAsUrl(resourceName: String): URL {
        val cls = SwapiResources::class.java
        return cls.getResource("/swapi/data/$resourceName.json")
    }
}