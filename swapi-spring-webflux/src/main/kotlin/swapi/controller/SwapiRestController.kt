package swapi.controller

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonBase
import com.beust.klaxon.JsonObject
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import swapi.logic.SwapiParser
import swapi.logic.SwapiResourceRelations
import java.io.FileNotFoundException
import java.net.InetAddress
import java.util.NoSuchElementException
import kotlin.reflect.full.findAnnotation

@RestController
@RequestMapping("/swapi")
class SwapiRestController(val environment: Environment) {

    private val logger = LoggerFactory.getLogger(SwapiRestController::class.qualifiedName)

    private val parser = SwapiParser()

    private val linkPrefix: String by lazy {
        // Local address
        //InetAddress.getLocalHost().hostAddress;
        //InetAddress.getLocalHost().hostName;

        // Remote address
        //val host = InetAddress.getLoopbackAddress().hostAddress;
        val host = InetAddress.getLoopbackAddress().hostName;
        val port = environment.getProperty("server.port");

        val requestMapping = this::class.findAnnotation<RequestMapping>()
        val pathPrefix = requestMapping?.let { a ->
            a.value.single()
        }

        //val urlPrefix = "http://$host:$port$pathPrefix"
        pathPrefix ?: ""
    }

    @GetMapping("/{resourceName}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun getAll(@PathVariable resourceName: String): List<JsonObject> {
        MDC.put("resource.name", resourceName)
        logger.debug("Enter: GET /{}", resourceName)
        try {
            var jsonArray = parser.parse<JsonArray<JsonObject>>("/swapi/data/$resourceName.json")
            //use yield instead?
            return jsonArray.map { json ->
                linkify(resourceName, json)
                json
            }
        } catch (fnf: FileNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown resource '$resourceName'")
        } finally {
            logger.debug("Leave: GET /{}", resourceName)
            MDC.remove("resource.name")
        }
    }

    @GetMapping("/{resourceName}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun getSingle(@PathVariable resourceName: String, @PathVariable id: String): JsonObject {
        MDC.put("resource.name", resourceName)
        MDC.put("resource.id", id)
        logger.debug("Enter: GET /{}/{}", resourceName, id)

        var jsonArray = parser.parse<JsonArray<JsonObject>>("/swapi/data/$resourceName.json")
        try {
            val result = jsonArray.single { json ->
                json["id"].toString() == id
            }
            linkify(resourceName, result)

            return result
        } catch (nse: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No resource with id=$id found in $resourceName")
        } catch (fnf: FileNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown resource '$resourceName'")
        } finally {
            logger.debug("Leave: GET /{}/{}", resourceName, id)
            MDC.remove("resource.name")
            MDC.remove("resource.id")
        }
    }

    private fun linkify(resourceName: String, json: JsonObject) {
        val relations = SwapiResourceRelations.getRelations(resourceName)
        relations.forEach { rel ->
            logger.trace("Linkify {} {}", resourceName, rel)
            val data = json[rel.source]
            data?.let {
                val links = linkify(rel, it as JsonBase)
                json[rel.source] = JsonArray<String>(links.toList())
            }
        }
    }

    private fun linkify(relation: SwapiResourceRelations.Relation, data: JsonBase) = sequence {
        val linkTemplate = "$linkPrefix/${relation.target}/"

        when (data) {
            is JsonObject -> {
                yield(linkTemplate + data["id"])
            }
            is JsonArray<*> -> {
                data.forEach {
                    val json = it as JsonObject
                    yield(linkTemplate + json["id"])
                }
            }
            else -> {
                logger.warn("$relation data is of unsupported type")
            }
        }
    }
}