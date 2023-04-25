//TEAM MEMBERS
//ALI HAZIME
//FATIMA KOURANI
//ZACHARY FAYBIK

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

    //grabs pokemon names from pokeapi
    fun fetchPokemonNames() {
        val queue = Volley.newRequestQueue(getApplication())
        val url = "https://pokeapi.co/api/v2/pokemon?limit=905"

        //grabs pokemon names
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
                    Log.e(TAG, "Error fetching Pokemon names: ${e.localizedMessage}")
                }
            },
            { error ->
                Log.e(TAG, "Error fetching Pokemon names: ${error.localizedMessage}")
            })

        queue.add(request)
    }

    //grabs Pokemon images, types, and descriptions
    fun fetchPokemonImagesAndText(
        pokemonId: String?,
        onSuccess: (
            pokemonData: JSONObject) -> Unit,
        onError: (
            errorMessage: String) -> Unit,
        isShiny: Boolean) {


        val queue = Volley.newRequestQueue(getApplication())
        val pokemonUrl = "https://pokeapi.co/api/v2/pokemon/$pokemonId"

        //string for if pokemon image is default or shiny
        var shinyString = "front_default"
        if(isShiny)
        {
            shinyString = "front_shiny"
        }

        //grabs pokemon name, image, type, and description
        val pokemonRequest = JsonObjectRequest(Request.Method.GET, pokemonUrl, null,
            { response ->
                // extract the relevant data from the response
                val pokemonData = JSONObject()
                pokemonData.put("name",
                    response.getString("name"))
                pokemonData.put("image_url",
                    response.getJSONObject("sprites").getString(shinyString))
                pokemonData.put("type",
                    response.getJSONArray("types").getJSONObject(0).getJSONObject("type").getString("name"))

                val pokemonUrlType = response.getJSONObject("species").getString("url")

                val pokemonType = JsonObjectRequest(Request.Method.GET, pokemonUrlType, null,
                    { pokeType ->
                        val pokemonFlavor = pokeType.getJSONArray("flavor_text_entries")
                        for (i in 0 until pokemonFlavor.length()) {
                            val entry = pokemonFlavor.getJSONObject(i)
                            val language = entry.getJSONObject("language").getString("name")
                            val flavorText = entry.getString("flavor_text")
                            if (language == "en") {
                                pokemonData.put("description", flavorText.replace("\n", " "))
                                break
                            }
                        }
                        onSuccess(pokemonData)
                    },
                    { typeError ->
                        onError(typeError.message ?: "ERROR: POKEMON TYPES NOT FOUND")
                    })
                queue.add(pokemonType)
            },
            { error ->
                onError(error.message ?: "ERROR: DATA NOT FOUND")
            })

        queue.add(pokemonRequest)
    }
    //fetches pokemon moves from api
    fun fetchPokemonMoves(
        pokemonId: String?,
        onSuccess: (moves: List<String>) -> Unit,
        onError: (errorMessage: String) -> Unit) {

        val queue = Volley.newRequestQueue(getApplication())
        val pokemonUrl = "https://pokeapi.co/api/v2/pokemon/$pokemonId"

        //grabs moves and sets the limit of moves grabbed to 6 for readability
        val pokemonRequest = JsonObjectRequest(Request.Method.GET, pokemonUrl, null,
            { response ->

                val movesList = mutableListOf<String>()
                val movesArray = response.getJSONArray("moves")
                val maxMoves = 6

                for (i in 0 until minOf(movesArray.length(), maxMoves)) {
                    val move = movesArray.getJSONObject(i)

                    //grabs move name from list of moves
                    val moveName = move.getJSONObject("move").getString("name")
                    movesList.add(moveName)
                }
                onSuccess(movesList)
            },
            { error ->
                onError(error.message ?: "ERROR: MOVES NOT FOUND")
            })

        queue.add(pokemonRequest)
    }
}

