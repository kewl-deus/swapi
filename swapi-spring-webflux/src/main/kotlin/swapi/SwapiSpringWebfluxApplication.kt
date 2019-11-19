package swapi

import com.beust.klaxon.Klaxon
import io.dekorate.kubernetes.annotation.ImagePullPolicy
import io.dekorate.kubernetes.annotation.KubernetesApplication
import io.dekorate.kubernetes.annotation.Probe
import io.dekorate.kubernetes.annotation.ServiceType
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.config.EnableWebFlux
import swapi.model.Film
import swapi.persistence.FilmRepository

@SpringBootApplication
@EnableWebFlux
@KubernetesApplication(
        livenessProbe = Probe(httpActionPath = "/actuator/health"),
        readinessProbe = Probe(httpActionPath = "/actuator/health"),
        serviceType = ServiceType.NodePort,
        imagePullPolicy = ImagePullPolicy.Always)
class SwapiSpringWebfluxApplication {
    private val log = LoggerFactory.getLogger(SwapiSpringWebfluxApplication::class.java)

    @Bean
    fun filmInsert(filmRepository: FilmRepository) = CommandLineRunner {
        val films = Klaxon().parseArray<Film>(SwapiResources.getResourceAsStream("films"))!!
        filmRepository.saveAll(films)
        films?.map { it.title }.forEach(System.out::println)
    }

}

fun main(args: Array<String>) {
    runApplication<SwapiSpringWebfluxApplication>(*args)
}





