package swapi

import io.dekorate.kubernetes.annotation.ImagePullPolicy
import io.dekorate.kubernetes.annotation.KubernetesApplication
import io.dekorate.kubernetes.annotation.Probe
import io.dekorate.kubernetes.annotation.ServiceType
import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
@KubernetesApplication(
        livenessProbe = Probe(httpActionPath = "/actuator/health"),
        readinessProbe = Probe(httpActionPath = "/actuator/health"),
        serviceType = ServiceType.NodePort,
        imagePullPolicy = ImagePullPolicy.Always)
class SwapiSpringWebfluxApplication


fun main(args: Array<String>) {
    runApplication<SwapiSpringWebfluxApplication>(*args)
}
