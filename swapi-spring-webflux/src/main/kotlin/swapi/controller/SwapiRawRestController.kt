package swapi.controller

import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import swapi.logic.SwapiResources


@RestController
@RequestMapping("/swapi/raw")
class SwapiRawRestController(val dataBufferFactory: DataBufferFactory) {

    private val logger = LoggerFactory.getLogger(SwapiRawRestController::class.qualifiedName)


    @GetMapping("/{resourceName}", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    @ResponseBody
    fun getRawData(@PathVariable resourceName: String): Flux<String> {
        //val resource = InputStreamResource(SwapiResources.getResourceAsStream(resourceName))
        val resource = UrlResource(SwapiResources.getResourceAsUrl(resourceName))
        return fluxify(resource)
    }

    private fun fluxify(resource: Resource): Flux<String> {
        val dataFlux = DataBufferUtils.read(resource, dataBufferFactory, 512)
        return dataFlux.map { dataBuffer ->
            dataBuffer.toString(Charsets.UTF_8)
        }

    }
}
