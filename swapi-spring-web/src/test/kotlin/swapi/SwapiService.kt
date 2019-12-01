package swapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface SwapiService {

    @GET("swapi/films")
    fun listFilms(): Call<List<String>>

    @GET("swapi/films/{id}")
    fun getFilm(@Path("id") id: String): Call<String>
}
