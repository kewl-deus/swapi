package swapi.persistence

import org.springframework.data.repository.PagingAndSortingRepository
import swapi.model.Film

interface FilmRepository : PagingAndSortingRepository<Film, String> {

    //fun findByTitle(title: String): Iterable<Film>
}