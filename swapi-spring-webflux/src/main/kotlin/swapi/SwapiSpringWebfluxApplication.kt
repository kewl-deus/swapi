package swapi

import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class SwapiSpringWebfluxApplication


fun main(args: Array<String>) {
    runApplication<SwapiSpringWebfluxApplication>(*args)
}
