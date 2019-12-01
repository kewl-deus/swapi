package swapi.controller

import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.nio.charset.Charset
import java.nio.file.Path
import java.nio.file.Paths


@RestController
@RequestMapping("/swapi/raw")
class SwapiRawRestController(val dataBufferFactory: DataBufferFactory) {

    private val logger = LoggerFactory.getLogger(SwapiRawRestController::class.qualifiedName)

    private val charset = Charset.forName("UTF-8")

    @GetMapping("/{resourceName}", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    @ResponseBody
    fun getRawData(@PathVariable resourceName: String): Flux<String> {
        logger.debug("Enter: GET /raw/{}", resourceName)

        val cls = SwapiRawRestController::class.java
        val resUrl = cls.getResource("/swapi/data/$resourceName.json")
        val resPath = Paths.get(resUrl.toURI())

        //val channel = Files.newByteChannel(resPath)

        logger.debug("Leave: /raw/{}", resourceName)
        return fluxify(resPath)
        //return fluxify(Files.newByteChannel(resPath))
    }

    private fun fluxify(sourcePath: Path): Flux<String> {
        //val stream = Files.lines(sourcePath)
        //return Flux.fromStream(stream)

        val dataFlux = DataBufferUtils.read(sourcePath, dataBufferFactory, 512)
        return dataFlux.map { dataBuffer ->
            dataBuffer.toString(charset)
        }

    }
}
