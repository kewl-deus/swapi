package swapi

import ch.sbb.esta.openshift.gracefullshutdown.GracefulshutdownSpringApplication
import com.beust.klaxon.Klaxon
import com.beust.klaxon.PropertyStrategy
import io.dekorate.kubernetes.annotation.ImagePullPolicy
import io.dekorate.kubernetes.annotation.KubernetesApplication
import io.dekorate.kubernetes.annotation.Probe
import io.dekorate.kubernetes.annotation.ServiceType
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.web.reactive.config.EnableWebFlux
import swapi.logic.SwapiResources
import swapi.model.Film
import swapi.persistence.FilmRepository
import kotlin.reflect.KProperty

@SpringBootApplication
@EnableWebFlux
@KubernetesApplication(
        livenessProbe = Probe(httpActionPath = "/actuator/info"),
        readinessProbe = Probe(httpActionPath = "/actuator/health"),
        serviceType = ServiceType.LoadBalancer,
        imagePullPolicy = ImagePullPolicy.Always)
        //sidecars = [Container(image = "docker.elastic.co/beats/filebeat:7.4.1", name = "filebeat")])
class SwapiSpringWebfluxApplication {
    private val logger = LoggerFactory.getLogger(SwapiSpringWebfluxApplication::class.java)

    @Bean
    fun filmInsert(filmRepository: FilmRepository) = CommandLineRunner {

        val films = Klaxon()
                .propertyStrategy { p: KProperty<*> -> p.name != "characters"}
                .parseArray<Film>(SwapiResources.getResourceAsStream("films"))!!


        filmRepository.saveAll(films)
        //films?.map { it.title }.forEach(System.out::println)
    }

    @Bean
    fun dataBufferFactory(): DataBufferFactory = DefaultDataBufferFactory()

}

fun Klaxon.propertyStrategy(ps: (KProperty<*>) -> Boolean): Klaxon = this.propertyStrategy(object: PropertyStrategy {
    override fun accept(property: KProperty<*>) = ps(property)
})

fun main(args: Array<String>) {
    //runApplication<SwapiSpringWebfluxApplication>(*args)
    GracefulshutdownSpringApplication.run(SwapiSpringWebfluxApplication::class.java, *args)
}





