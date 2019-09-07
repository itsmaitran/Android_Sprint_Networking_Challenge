package com.lambdaschool.pokemonapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lambdaschool.pokemonapi.Constants
import com.lambdaschool.pokemonapi.R
import com.lambdaschool.pokemonapi.model.PokemonData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_pokemon.*

class ViewPokemonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pokemon)

        val pokemonData = intent.getSerializableExtra(Constants.POKEMON_INTENT_KEY) as PokemonData
        val abilities = pokemonData.abilities.toString().replace("[", "").replace("]", "")
        val types = pokemonData.types.toString().replace("[", "").replace("]","")

        tv_pokemon_name.text = "${pokemonData.name}"
        tv_pokemon_id.text = "ID: ${pokemonData.id}"
        Picasso.get().load(pokemonData.sprites).into(iv_pokemon_sprite)
        tv_pokemon_abilities.text = "Abilities: $abilities"
        tv_pokemon_types.text = "Types: $types"

    }
}