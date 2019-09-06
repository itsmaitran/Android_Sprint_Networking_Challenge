package com.lambdaschool.pokemonapi.retrofit

import android.graphics.Bitmap
import com.google.gson.GsonBuilder
import com.lambdaschool.pokemonapi.adapter.NetworkAdapter
import com.lambdaschool.pokemonapi.model.Pokemon
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface PokemonAPI {
    @GET("/")
    fun getPokemon() : Call<List<Pokemon>>

    class Factory {

        companion object {

            private const val BASE_URL = "https://pokeapi.co/api/v2/pokemon/"

            fun getPokemon(nameorId: String): JSONObject? {
                var pokemonJSON: JSONObject? = null
                val pokemonUrl = BASE_URL + nameorId
                val result = NetworkAdapter.httpGETRequest(pokemonUrl)
                    try {
                        pokemonJSON = JSONObject(result)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

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
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build()

                val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

                val pokemonAPI = retrofit.create(PokemonAPI::class.java)

                return pokemonJSON
            }

            fun getPokemonSprite(spriteUrl: String): Bitmap? {
                return NetworkAdapter.getBitmapFromURL(spriteUrl)
            }
        }
    }
}