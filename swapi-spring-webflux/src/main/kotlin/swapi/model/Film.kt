package swapi.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinTable
import javax.persistence.ManyToMany

@Suppress("JpaObjectClassSignatureInspection")
@Entity
data class Film(
        @Id
        val id: String,

        @Column(nullable = false)
        val episodeId: Int,

        @Column(nullable = false)
        val title: String,

        @Column(length = 2000)
        val openingCrawl: String? = null,

        val director: String? = null,

        @ManyToMany
        @JoinTable(name = "FILM_CHARACTERS")
        val characters: Set<Person>? = null
)

/*
    private val filmRelations = listOf(
            Multiple("characters", "persons"), Multiple("planets"),
            Multiple("species"), Multiple("starships"), Multiple("vehicles")
    )
*/