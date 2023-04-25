package com.example.pokedexapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.pokedexapp.databinding.FragmentInfoBinding
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
            Log.i("INFOFRAG", "ERROR: $errorMessage")
            // handle error
        }, false)
        binding.shinySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Switch is ON
                isShiny = true
                viewModel.fetchPokemonImagesAndText(selectedPokemonName, { pokemonData ->
                    pokeTextView.text = "Type: " + pokemonData.getString("type")
                    pokeDescriptionView.text = "Description: " + pokemonData.getString("description")
                    Glide.with(this)
                        .load(pokemonData.getString("image_url"))
                        .into(pokePicHolder)
                }, { errorMessage ->
                    Log.i("INFOFRAG", "ERROR: $errorMessage")
                    // handle error
                }, isShiny)
            } else {
                isShiny = false
                viewModel.fetchPokemonImagesAndText(selectedPokemonName, { pokemonData ->
                    pokeTextView.text = "Type: " + pokemonData.getString("type")
                    pokeDescriptionView.text = "Description: " + pokemonData.getString("description")
                    Glide.with(this)
                        .load(pokemonData.getString("image_url"))
                        .into(pokePicHolder)
                }, { errorMessage ->
                    Log.i("INFOFRAG", "ERROR: $errorMessage")
                    // handle error
                }, isShiny)
            }
        }

        return binding.root
    }


}