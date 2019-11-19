package swapi

import com.beust.klaxon.Parser
import java.io.FileNotFoundException
import java.util.*

class SwapiParser {

    fun <T> parse(resourceFile: String): T {
        val cls = SwapiParser::class.java
        val inputStream = cls.getResourceAsStream(resourceFile)
        if (Objects.isNull(inputStream)) throw FileNotFoundException(resourceFile)
        return Parser.default().parse(inputStream) as T
    }
}