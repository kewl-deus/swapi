package swapi.persistence

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import swapi.model.Person

@Repository
interface PersonRepository : PagingAndSortingRepository<Person, String> {
}