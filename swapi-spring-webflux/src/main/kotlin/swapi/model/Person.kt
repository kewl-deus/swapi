package swapi.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class Person(
        @Id
        val id: String,

        @Column(nullable = false)
        val name: String,

        val mass: Int,

        val brithYear: String,

        @Enumerated(EnumType.STRING)
        val gender: Gender,

        val hairColor: String,

        //val skinColor: Set<String>

        //val eyeColor: Set<String>

        //val homeworld: Planet

        //val species: List<Species>

        //val starships: List<Starship>

        //val vehicles: List<Vehicle>

        @ManyToMany(mappedBy = "characters")
        val films: List<Film>
)

/*

    private val personRelations = listOf(
            Multiple("films"), Single("homeworld", "planets"), Multiple("species"),
            Multiple("starships"), Multiple("vehicles")

{
        "species": [
        ],
        "mass": 77,
        "name": "Luke Skywalker",
        "hairColor": [
          "BLONDE"
        ],
        "homeworld": {
        },
        "height": 172,
        "vehicles": [
          {
        ],
        "birthYear": "19BBY",
        "films": [
        ],
        "skinColor": [
          "FAIR"
        ],
        "id": "cj0nv9p8yewci0130wjy4o5fa",
        "starships": [
        ],
        "eyeColor": [
          "BLUE"
        ],
        "gender": "MALE"
      }

 */