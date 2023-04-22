package com.example.pokedexapp.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "MainViewModel"
    val pokemonNames = MutableLiveData<List<String>>()
    val error = MutableLiveData<String>()

    val filteredPokemonNames = MutableLiveData<List<String>>()

    fun filterPokemonNames(query: String) {
        val filteredList = pokemonNames.value?.filter { it.contains(query, ignoreCase = true) }
        filteredPokemonNames.value = filteredList!!
    }

    fun fetchPokemonNames() {
        val queue = Volley.newRequestQueue(getApplication())
        val url = "https://pokeapi.co/api/v2/pokemon?limit=1000"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val results = response.getJSONArray("results")
                    val names = mutableListOf<String>()
                    for (i in 0 until results.length()) {
                        val pokemon = results.getJSONObject(i)
                        val name = pokemon.getString("name")
                        names.add(name)
                    }

                    pokemonNames.value = names
                    filteredPokemonNames.value = names

                    Log.d(TAG, "Received Pokemon names in MainViewModel: $names")
                } catch (e: JSONException) {
                    error.value = "Error parsing JSON response: ${e.localizedMessage}"
                    Log.e(TAG, "Error parsing JSON response: ${e.localizedMessage}")
                }
            },
            { error ->
                this.error.value = "Error fetching Pokemon names: ${error.localizedMessage}"
                Log.e(TAG, "Error fetching Pokemon names: ${error.localizedMessage}")
            })

        queue.add(request)
    }

    fun fetchPokemonImagesAndText(pokemonId: String?, onSuccess: (pokemonData: JSONObject) -> Unit, onError: (errorMessage: String) -> Unit) {
        val queue = Volley.newRequestQueue(getApplication())
        val pokemonUrl = "https://pokeapi.co/api/v2/pokemon/$pokemonId"

        val pokemonRequest = JsonObjectRequest(Request.Method.GET, pokemonUrl, null,
            { response ->
                // extract the relevant data from the response
                val pokemonData = JSONObject()
                pokemonData.put("name", response.getString("name"))
                pokemonData.put("image_url", response.getJSONObject("sprites").getString("front_default"))
                pokemonData.put("type", response.getJSONArray("types").getJSONObject(0).getJSONObject("type").getString("name"))

                val speciesUrl = response.getJSONObject("species").getString("url")
                val speciesRequest = JsonObjectRequest(Request.Method.GET, speciesUrl, null,
                    { speciesResponse ->
                        val flavorTextEntries = speciesResponse.getJSONArray("flavor_text_entries")
                        for (i in 0 until flavorTextEntries.length()) {
                            val entry = flavorTextEntries.getJSONObject(i)
                            val language = entry.getJSONObject("language").getString("name")
                            val text = entry.getString("flavor_text")
                            if (language == "en") {
                                pokemonData.put("description", text.replace("\n", " "))
                                break
                            }
                        }
                        onSuccess(pokemonData)
                    },
                    { speciesError ->
                        onError(speciesError.message ?: "Error fetching Pokemon species data")
                    })
                queue.add(speciesRequest)
            },
            { error ->
                onError(error.message ?: "Error fetching Pokemon data")
            })

        queue.add(pokemonRequest)
    }

}

