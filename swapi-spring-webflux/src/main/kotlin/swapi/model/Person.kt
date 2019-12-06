package swapi.model

import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
data class Person(
        @Id
        val id: String,

        @Column(nullable = false)
        val name: String,

        val mass: Int,

        val birthYear: String,

        @Enumerated(EnumType.STRING)
        val gender: Gender,

        @ElementCollection
        val hairColor: Set<HairColor>? = null,

        @ElementCollection
        val skinColor: Set<SkinColor>? = null,

        @ElementCollection
        val eyeColor: Set<EyeColor>? = null,

        //val homeworld: Planet

        //val species: Set<Species>

        //val starships: Set<Starship>

        //val vehicles: Set<Vehicle>

        @ManyToMany(mappedBy = "characters")
        val films: Set<Film>? = null
)

enum class Gender {
    MALE,
    FEMALE,
    UNKNOWN
}

@Embeddable
data class HairColor(val value: String)

@Embeddable
data class SkinColor(val value: String)

@Embeddable
data class EyeColor(val value: String)
