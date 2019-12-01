package swapi

import ch.sbb.esta.openshift.gracefullshutdown.GracefulshutdownSpringApplication
import com.beust.klaxon.Klaxon
import io.dekorate.kubernetes.annotation.Container
import io.dekorate.kubernetes.annotation.ImagePullPolicy
import io.dekorate.kubernetes.annotation.KubernetesApplication
import io.dekorate.kubernetes.annotation.Probe
import io.dekorate.kubernetes.annotation.ServiceType
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.web.reactive.config.EnableWebFlux
import swapi.logic.SwapiResources
import swapi.model.Film
import swapi.persistence.FilmRepository


@SpringBootApplication
@EnableWebFlux
@KubernetesApplication(
        livenessProbe = Probe(httpActionPath = "/actuator/info"),
        readinessProbe = Probe(httpActionPath = "/actuator/health"),
        serviceType = ServiceType.LoadBalancer,
        imagePullPolicy = ImagePullPolicy.Always,
        sidecars = [Container(image = "docker.elastic.co/beats/filebeat:7.4.1", name = "filebeat")])
class SwapiSpringWebfluxApplication {
    private val logger = LoggerFactory.getLogger(SwapiSpringWebfluxApplication::class.java)

    @Bean
    fun filmInsert(filmRepository: FilmRepository) = CommandLineRunner {
        val films = Klaxon().parseArray<Film>(SwapiResources.getResourceAsStream("films"))!!
        filmRepository.saveAll(films)
        //films?.map { it.title }.forEach(System.out::println)
    }

    @Bean
    fun dataBufferFactory(): DataBufferFactory = DefaultDataBufferFactory()

    /*
    @Bean
    fun requestLoggingFilter(): CommonsRequestLoggingFilter? {
        val filter = CommonsRequestLoggingFilter()
        filter.setIncludeQueryString(true)
        filter.setIncludePayload(true)
        filter.setMaxPayloadLength(10000)
        filter.setIncludeHeaders(true)
        filter.setAfterMessagePrefix("REQUEST DATA : ")
        return filter
    }
    */
}

fun main(args: Array<String>) {
    //runApplication<SwapiSpringWebfluxApplication>(*args)
    GracefulshutdownSpringApplication.run(SwapiSpringWebfluxApplication::class.java, *args)
}





