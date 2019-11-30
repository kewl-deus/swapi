package swapi

object SwapiResourceRelations {

    abstract class Relation(val source: String, val target: String) {
        constructor(sourceAndTarget: String) : this(sourceAndTarget, sourceAndTarget)

        override fun toString(): String {
            return "Relation(source='$source', target='$target')"
        }

    }

    class Single(source: String, target: String) : Relation(source, target) {
        constructor(sourceAndTarget: String) : this(sourceAndTarget, sourceAndTarget)
    }

    class Multiple(source: String, target: String) : Relation(source, target) {
        constructor(sourceAndTarget: String) : this(sourceAndTarget, sourceAndTarget)
    }

    private val filmRelations = listOf(
            Multiple("characters", "persons"), Multiple("planets"),
            Multiple("species"), Multiple("starships"), Multiple("vehicles")
    )

    private val personRelations = listOf(
            Multiple("films"), Single("homeworld", "planets"), Multiple("species"),
            Multiple("starships"), Multiple("vehicles")
    )

    private val planetRelations = listOf(
            Multiple("films"), Multiple("residents", "persons")
    )

    private val speciesRelations = listOf(
            Multiple("films"), Multiple("people", "persons")
    )

    private val starshipRelations = listOf(
            Multiple("films"), Multiple("pilots", "persons")
    )

    private val vehicleRelations = listOf(
            Multiple("films"), Multiple("pilots", "persons")
    )

    private val relationTypeMap = mapOf("films" to filmRelations,
            "persons" to personRelations, "planets" to planetRelations, "species" to speciesRelations,
            "starships" to starshipRelations, "vehicles" to vehicleRelations)

    fun getRelations(resource: String) = relationTypeMap.getOrElse(resource, {emptyList()})

    fun resolve(sourceResourceType: String, relationName: String): Relation =
        relationTypeMap.getOrElse(sourceResourceType, {emptyList()}).single { rel -> rel.source == relationName }

}
