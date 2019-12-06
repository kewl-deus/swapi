package swapi

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import swapi.logic.SwapiResourceRelations

class TestSwapiResourceRelations {

    @Test
    fun shouldMatchPersonRelations() {
        Assertions.assertEquals(
                listOf("films", "homeworld", "species", "starships", "vehicles"),
                SwapiResourceRelations.getRelations("persons").map { r -> r.source })
    }
}