package swapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
class HealthController {

    /**
     * Liveness Probe
     */
    @GetMapping("alive")
    fun isAlive(): String {
        return "OK"
    }
}