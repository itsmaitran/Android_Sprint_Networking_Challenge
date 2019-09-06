package com.lambdaschool.pokemonapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.lambdaschool.pokemonapi.adapter.RecyclerViewAdapter
import com.lambdaschool.pokemonapi.model.Pokemon
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), Callback<List<Pokemon>> {
    override fun onFailure(call: Call<List<Pokemon>>, t: Throwable) {
        t.printStackTrace()
        val response = "Failure; ${t.printStackTrace()}"
        Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(call: Call<List<Pokemon>>, response: Response<List<Pokemon>>) {
        if (response.isSuccessful) {
            val pokemonList = response.body() as ArrayList<Pokemon>
            rv_list.apply {
                setHasFixedSize(false)
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = RecyclerViewAdapter(pokemonList)
            }
        } else {
            val response = "Response not successful; ${response.errorBody().toString()}"
            Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
