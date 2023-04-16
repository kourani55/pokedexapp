package com.example.pokedexapp

import android.app.Activity
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pokedexapp.databinding.ActivityMainBinding
import com.example.pokedexapp.ui.main.MainFragment
import com.example.pokedexapp.ui.main.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Initialize the view model
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Observe the pokemonNames LiveData
        viewModel.pokemonNames.observe(this, Observer { pokemonNames ->
            // Update your UI or perform any other operations with the received Pokemon names
            Log.d(TAG, "Received Pokemon names in Main Activity: $pokemonNames")
        })

        // Fetch the Pokemon names
        viewModel.fetchPokemonNames()
    }
}