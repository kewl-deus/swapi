package swapi.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Film(
        @Id
        val id: String,

        val episodeId: Int,

        val title: String,

        @Column(length = 2000)
        val openingCrawl: String,

        val director: String
)
