//TEAM MEMBERS
//ALI HAZIME
//FATIMA KOURANI
//ZACHARY FAYBIK

package com.example.pokedexapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.pokedexapp.databinding.FragmentInfoBinding
class InfoFragment : Fragment() {


    private lateinit var binding : FragmentInfoBinding
    private lateinit var pokePicHolder : ImageView
    private lateinit var pokeTextView: TextView
    private lateinit var pokeDescriptionView : TextView
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var isShiny = false

        // Inflate the layout for this fragment
        binding = FragmentInfoBinding.inflate(inflater, container, false)

        val selectedPokemonName = arguments?.getString("selectedPokemonName")

        pokePicHolder = binding.pokePic
        pokeTextView = binding.pokeName
        pokeDescriptionView = binding.pokeDescription

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        viewModel.fetchPokemonImagesAndText(selectedPokemonName, { pokemonData ->

            pokeTextView.text = "Type: " + pokemonData.getString("type")
            pokeDescriptionView.text = "Description: " + pokemonData.getString("description")

            Glide.with(this)
                .load(pokemonData.getString("image_url"))
                .into(pokePicHolder)

        }, { errorMessage ->
            Log.i("Pokemon Image", "Error: $errorMessage")
            // handle error
        }, false)

        //switch listener for shiny pokemon image
        binding.shinySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                // Switch is ON
                isShiny = true

                //loads shiny pokemon image using glide
                viewModel.fetchPokemonImagesAndText(selectedPokemonName, { pokemonData ->
                    pokeTextView.text = "Type: " + pokemonData.getString("type")
                    pokeDescriptionView.text = "Description: " + pokemonData.getString("description")

                    Glide.with(this)
                        .load(pokemonData.getString("image_url"))
                        .into(pokePicHolder)
                }, { errorMessage ->
                    Log.i("Shiny Pokemon Image", "Error: $errorMessage")
                }, isShiny)

            } else {

                //SHINY IS OFF
                isShiny = false
                viewModel.fetchPokemonImagesAndText(selectedPokemonName, { pokemonData ->
                    pokeTextView.text = "Type: " + pokemonData.getString("type")
                    pokeDescriptionView.text = "Description: " + pokemonData.getString("description")
                    Glide.with(this)
                        .load(pokemonData.getString("image_url"))
                        .into(pokePicHolder)
                }, { errorMessage ->
                    Log.i("Shiny is Off", "Error: $errorMessage")
                }, isShiny)
            }
        }
        binding.movesetBtn.setOnClickListener {
            viewModel.fetchPokemonMoves(selectedPokemonName, { moves ->

                //separates moves using ,
                val pokeMoveText = moves.joinToString(", ")
                binding.pokeMoves.text = pokeMoveText
            }, {errorMessage ->
                Log.i("POKEMOVES", "ERROR: $errorMessage")
            })
        }

        return binding.root
    }


}