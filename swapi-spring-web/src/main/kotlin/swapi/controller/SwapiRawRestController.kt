package swapi.controller

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.Charset
import java.nio.file.Paths


@RestController
@RequestMapping("/swapi/raw")
class SwapiRawRestController() {

    private val logger = LoggerFactory.getLogger(SwapiRawRestController::class.qualifiedName)

    private val charset = Charset.forName("UTF-8")

    @GetMapping("/{resourceName}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun getRawData(@PathVariable resourceName: String): String {
        logger.debug("Enter: GET /raw/{}", resourceName)

        val cls = SwapiRawRestController::class.java
        val resUrl = cls.getResource("/swapi/data/$resourceName.json")
        val resPath = Paths.get(resUrl.toURI())

        logger.debug("Leave: /raw/{}", resourceName)
        return resPath.toFile().readText()
    }

}
