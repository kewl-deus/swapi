package swapi

import java.io.InputStream

object SwapiResources {

    fun getResourceAsStream(resourceName: String): InputStream {
        val cls = SwapiResources::class.java
        return cls.getResourceAsStream("/swapi/data/$resourceName.json")
    }
}