//TEAM MEMBERS
//ALI HAZIME
//FATIMA KOURANI
//ZACHARY FAYBIK

package com.example.pokedexapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pokedexapp.databinding.ActivityMainBinding
import com.example.pokedexapp.ui.main.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //gets pokemon names from view model
        viewModel.pokemonNames.observe(this, Observer { pokemonNames ->

            Log.d(TAG, "Received Pokemon names in Main Activity: $pokemonNames")

        })

        //fetch pokemon names
        viewModel.fetchPokemonNames()
    }
}