package swapi.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import swapi.logic.SwapiResources


@RestController
@RequestMapping("/swapi/raw")
class SwapiRawRestController() {

    @GetMapping("/{resourceName}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun getRawData(@PathVariable resourceName: String): String {
        return SwapiResources.getResourceAsStream(resourceName).reader(Charsets.UTF_8).readText()
    }

}
