package swapi

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonBase
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import swapi.util.TimerClock
import java.io.FileNotFoundException
import java.net.InetAddress
import java.util.*

@RestController
@RequestMapping("/swapi")
class SwapiRestController(val environment: Environment) {

    private val LOG = LoggerFactory.getLogger(SwapiRestController::class.qualifiedName)

    private val parser = SwapiParser()

    @GetMapping("/{resourceName}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun getAll(@PathVariable resourceName: String): Flux<JsonObject> {
        LOG.trace("GET /$resourceName")
        val timer = TimerClock().start()

        try {
            var jsonArray = parser.parse<JsonArray<JsonObject>>("/swapi/data/$resourceName.json")

            //return Flux.fromIterable(jsonArray)
            return Flux.create<JsonObject> { emitter ->
                jsonArray.forEach { json ->
                    LOG.trace("Emitting: {} {}", resourceName, json["id"])
                    linkify(resourceName, json)
                    emitter.next(json)
                }
                emitter.complete()
            }
        } catch (fnf: FileNotFoundException){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown resource '$resourceName'")
        } finally {
            LOG.trace("GET /$resourceName took ${timer.stop().durationSeconds()}")
        }
    }

    @GetMapping("/{resourceName}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun getSingle(@PathVariable resourceName: String, @PathVariable id: String): Mono<JsonObject> {
        LOG.trace("GET /$resourceName/$id")
        val timer = TimerClock().start()

        var jsonArray = parser.parse<JsonArray<JsonObject>>("/swapi/data/$resourceName.json")
        try {
            val result = jsonArray.single { json ->
                json["id"].toString() == id
            }
            linkify(resourceName, result)

            return Mono.just(result)
        } catch (nse: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No resource with id=$id found in $resourceName")
        } catch (fnf: FileNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown resource '$resourceName'")
        } finally {
            LOG.trace("GET /$resourceName/$id took ${timer.stop().durationSeconds()}")
        }
    }

    private fun linkify(resourceName: String, json: JsonObject) {
        val relations = SwapiResourceRelations.getRelations(resourceName)
        relations.forEach { rel ->
            LOG.trace("Linkify $resourceName $rel")
            val data = json[rel.source]
            data?.let {
                val links = linkify(rel, it as JsonBase)
                val linkJsonArray = JsonArray<String>()
                links.subscribe({ link -> linkJsonArray.add(link) }, null, { json[rel.source] = linkJsonArray })
            }
        }
    }

    private fun linkify(relation: SwapiResourceRelations.Relation, data: JsonBase): Flux<String> {
        // Local address
        //InetAddress.getLocalHost().hostAddress;
        //InetAddress.getLocalHost().hostName;

        // Remote address
        //val host = InetAddress.getLoopbackAddress().hostAddress;
        val host = InetAddress.getLoopbackAddress().hostName;
        val port = environment.getProperty("server.port");

        //val linkTemplate = "http://$host:$port/swapi/${relation.target}/"
        val linkTemplate = "/swapi/${relation.target}/"

        return Flux.create { emitter ->
            when (data) {
                is JsonObject -> {
                    emitter.next(linkTemplate + data["id"])
                }
                is JsonArray<*> -> {
                    data.forEach {
                        val json = it as JsonObject
                        emitter.next(linkTemplate + json["id"])
                    }
                }
                else -> {
                    LOG.warn("$relation data is of unsupported type")
                }
            }
            emitter.complete()
        }
    }

}
