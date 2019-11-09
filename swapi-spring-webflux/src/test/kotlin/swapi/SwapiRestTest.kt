package swapi

import com.beust.klaxon.JsonObject
import org.junit.jupiter.api.Test
import java.time.LocalTime
import retrofit2.Retrofit
import retrofit2.create


class SwapiRestTest {

    private val DEBUG: Boolean = true

    private val baseUrl = "https://swapi.co/api"

    private fun println(message: Any?) {
        var msg = message
        if (DEBUG) {
            val threadName = Thread.currentThread().name
            val time = LocalTime.now()
            msg = "[$threadName $time]: $message"
        }
        System.out.println(msg)
    }

    @Test
    fun shouldListTallSpecies(){
        val client = Retrofit.Builder()
                .baseUrl("http://localhost:8080/swapi/")
                .build()

        val swapi = client.create<SwapiService>()
        val films = swapi.listFilms().execute()
        println(films)
    }
}
