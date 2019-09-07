package com.lambdaschool.pokemonapi.retrofit

import com.google.gson.GsonBuilder
import com.lambdaschool.pokemonapi.model.JSONPokemon
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface PokemonAPI {

    @GET("pokemon/{name-id}")
    fun getPokemon(@Path("name-id") nameOrId: String) : Call<JSONPokemon>

    companion object {
        private const val BASE_URL = "https://pokeapi.co/api/v2/"

        fun create(): PokemonAPI {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
                level = HttpLoggingInterceptor.Level.BODY
            }

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logger)
                .retryOnConnectionFailure(false)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit.create(PokemonAPI::class.java)
        }
    }
}
