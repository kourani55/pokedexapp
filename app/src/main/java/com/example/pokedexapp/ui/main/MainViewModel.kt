package com.example.pokedexapp.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "MainViewModel"
    val pokemonNames = MutableLiveData<List<String>>()
    val error = MutableLiveData<String>()

    val filteredPokemonNames = MutableLiveData<List<String>>()

    fun filterPokemonNames(query: String) {
        val filteredList = pokemonNames.value?.filter { it.contains(query, ignoreCase = true) }
        filteredPokemonNames.value = filteredList
    }

    fun fetchPokemonNames() {
        val queue = Volley.newRequestQueue(getApplication())
        val url = "https://pokeapi.co/api/v2/pokemon?limit=1000"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val results = response.getJSONArray("results")
                    val names = mutableListOf<String>()
                    for (i in 0 until results.length()) {
                        val pokemon = results.getJSONObject(i)
                        val name = pokemon.getString("name")
                        names.add(name)
                    }
                    pokemonNames.value = names
                    Log.d(TAG, "Received Pokemon names: $names")
                } catch (e: JSONException) {
                    error.value = "Error parsing JSON response: ${e.localizedMessage}"
                    Log.e(TAG, "Error parsing JSON response: ${e.localizedMessage}")
                }
            },
            Response.ErrorListener { error ->
                this.error.value = "Error fetching Pokemon names: ${error.localizedMessage}"
                Log.e(TAG, "Error fetching Pokemon names: ${error.localizedMessage}")
            })

        queue.add(request)
    }
}