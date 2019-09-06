package com.lambdaschool.pokemonapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.lambdaschool.pokemonapi.model.Pokemon
import com.lambdaschool.pokemonapi.retrofit.PokemonAPI

class ViewPokemonActivity : AppCompatActivity() {

    private var pokemon: Pokemon? = null
    private var pokemonName: TextView? = null
    private var pokemonId: TextView? = null
    private var pokemonSprite: ImageView? = null
    private var pokemonAbilities: TextView? = null
    private var pokemonTypes: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pokemon)

        pokemonName = findViewById(R.id.tv_pokemon_name)
        pokemonId = findViewById(R.id.tv_pokemon_id)
        pokemonSprite = findViewById(R.id.iv_pokemon_sprite)
        pokemonAbilities = findViewById(R.id.tv_pokemon_abilities)
        pokemonTypes = findViewById(R.id.tv_header_types)

        val intent = intent
        pokemon = intent.getParcelableExtra(Constants.POKEMON_INTENT_KEY)

        Thread(Runnable {
            val sprite = PokemonAPI.Factory.getPokemonSprite(pokemon?.spriteUrl)
            val abilities = parseToNewLines(pokemon?.abilities)
            val types = parseToNewLines(pokemon?.types)

            // May need to fix.
            runOnUiThread {
                pokemonName?.setText(pokemon?.name)
                pokemonId?.text = Integer.toString(pokemon?.id)
                pokemonSprite?.setImageBitmap(sprite)
                pokemonAbilities?.text = abilities
                pokemonTypes?.text = types
            }
        }).start()
    }

    private fun parseToNewLines(list: ArrayList<String>): String {
        val builder = StringBuilder()
        for (i in list.indices) {
            builder.append(list[i]).append("\n")
        }

        return builder.toString()
    }
}
