package com.lambdaschool.pokemonapi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.lambdaschool.pokemonapi.Constants.POKEMON_INTENT_KEY
import com.lambdaschool.pokemonapi.R
import com.lambdaschool.pokemonapi.adapter.RecyclerViewAdapter
import com.lambdaschool.pokemonapi.model.JSONPokemon
import com.lambdaschool.pokemonapi.model.PokemonData
import com.lambdaschool.pokemonapi.retrofit.PokemonAPI
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), Callback<JSONPokemon> {

    val pokemonList = mutableListOf<PokemonData>()
    lateinit var pokemonAPI: PokemonAPI

    override fun onFailure(call: Call<JSONPokemon>, t: Throwable) {
        t.printStackTrace()
        val response = "Failure; ${t.printStackTrace()}"
        Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(call: Call<JSONPokemon>, response: Response<JSONPokemon>) {
        if (response.isSuccessful) {
            val jsonPokemon = response.body() as JSONPokemon
            val abilities = mutableListOf<String>()
            jsonPokemon.abilities.forEach {
                abilities.add(it.ability.name)
            }

            val types = mutableListOf<String>()
            jsonPokemon.types.forEach {
                types.add(it.type.name)
            }

            val pokemonData = PokemonData(
                jsonPokemon.name,
                jsonPokemon.sprites.front_default,
                jsonPokemon.id,
                abilities,
                types
            )

            pokemonList.add(pokemonData)
            rv_list.adapter!!.notifyDataSetChanged()

            val intent = Intent(this, ViewPokemonActivity::class.java)
            intent.putExtra(POKEMON_INTENT_KEY, pokemonData)
            startActivity(intent)

        } else {
            val response = "Response not successful; ${response.errorBody().toString()}"
            Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchPokemon(nameOrId: String) {
        pokemonAPI.getPokemon(nameOrId).enqueue(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pokemonAPI = PokemonAPI.create()

        rv_list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = RecyclerViewAdapter(pokemonList)
        }

        iv_search.setOnClickListener {
            val pokemonText = et_search.text.toString()
            searchPokemon(pokemonText)
        }
    }
}
