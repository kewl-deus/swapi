package swapi.persistence

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import swapi.model.Film

@Repository
interface FilmRepository : PagingAndSortingRepository<Film, String> {

    //fun findByTitle(title: String): Film

    fun findByEpisodeId(episodeId: Int): Film?
}