package com.example.pokedexapp.ui.main

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pokedexapp.R
import com.example.pokedexapp.databinding.FragmentMainBinding
import org.json.JSONObject

class MainFragment : Fragment() {



    private  lateinit var binding : FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private var activityCallback: searchSpinnerListener?=null

    interface searchSpinnerListener {
        fun pokemonSelect(text: JSONObject)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun onPokemonSelected(text: JSONObject) {
        activityCallback?.pokemonSelect(text)
    }

}


