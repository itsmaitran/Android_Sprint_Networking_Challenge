package com.lambdaschool.pokemonapi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lambdaschool.pokemonapi.Constants.POKEMON_INTENT_KEY
import com.lambdaschool.pokemonapi.R
import com.lambdaschool.pokemonapi.ViewPokemonActivity
import com.lambdaschool.pokemonapi.model.Pokemon

class RecyclerViewAdapter (var searchedPokemons: ArrayList<Pokemon>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var pokemonList: LinearLayout = view.findViewById(R.id.ll_pokemon_list)
        var pokemonName: TextView = view.findViewById(R.id.tv_saved_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewGroup = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_list, parent, false)
        return ViewHolder(viewGroup)
    }

    override fun getItemCount(): Int {
        return searchedPokemons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = searchedPokemons[position]
        holder.pokemonName.setText(pokemon.name?.toUpperCase())
        holder.pokemonList.setOnClickListener {
            var intent = Intent(this, ViewPokemonActivity::class.java)
            intent.putExtra(this, POKEMON_INTENT_KEY, pokemon)
            this.startActivity(intent)
        }
        holder.pokemonList.setOnLongClickListener {
            searchedPokemons.removeAt(position)
            notifyItemRemoved(position)
            true
        }
    }
}