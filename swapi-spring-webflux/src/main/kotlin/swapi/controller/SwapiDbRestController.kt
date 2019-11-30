package swapi.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import swapi.model.Film
import swapi.persistence.FilmRepository

@RestController
@RequestMapping("/swapi/db")
class SwapiDbRestController(
        @Autowired
        val filmRepository: FilmRepository) {

    private val LOG = LoggerFactory.getLogger(SwapiDbRestController::class.qualifiedName)

    //@GetMapping("/films/all", produces = [MediaType.APPLICATION_JSON_VALUE])
    //fun getFilms() = filmRepository.findAll()

    @GetMapping("/films/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getFilm(@PathVariable id: String) = filmRepository.findByIdOrNull(id)

    //@GetMapping("/films", produces = [MediaType.APPLICATION_JSON_VALUE])
    //fun getFilmByEpisodeId(@RequestParam(name="episode") episodeId: Int) = filmRepository.findByEpisodeId(episodeId)

    @GetMapping("/films", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getFilms(@RequestParam(name = "episode", required = false) episodeId: Int?): Iterable<Film>  {
        if (episodeId != null) {
            val film = filmRepository.findByEpisodeId(episodeId)
            return listOfNotNull(film)
        }
        return filmRepository.findAll()
    }
}