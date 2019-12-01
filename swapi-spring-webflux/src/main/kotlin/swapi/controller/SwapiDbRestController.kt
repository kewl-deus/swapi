package swapi.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import swapi.model.Film
import swapi.persistence.FilmRepository

@RestController
@RequestMapping("/swapi/db")
class SwapiDbRestController(
        @Autowired
        val filmRepository: FilmRepository) {

    private val logger = LoggerFactory.getLogger(SwapiDbRestController::class.qualifiedName)

    @GetMapping("/films/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getFilm(@PathVariable id: String): Mono<Film> {
        return Mono.create<Film> { emitter ->
            val film = filmRepository.findByIdOrNull(id)
            if (film == null) {
                emitter.error(ResponseStatusException(HttpStatus.NOT_FOUND, "Film with id=$id does not exist"))
            } else {
                emitter.success(film)
            }
        }
    }

    @GetMapping("/films", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getFilms(@RequestParam(name = "episode", required = false) episodeId: Int?): Flux<Film> {
        if (episodeId != null) {
            val film = filmRepository.findByEpisodeId(episodeId)
            //return listOfNotNull(film)
            return Flux.create<Film> { emitter ->
                film?.let {
                    emitter.next(it)
                }
                emitter.complete()
            }
        }
        return Flux.fromIterable(filmRepository.findAll())
    }
}