package com.example.pokedexapp.ui.main

import android.content.ContentValues.TAG
import android.icu.text.IDNA.Info
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.pokedexapp.R
import com.example.pokedexapp.databinding.FragmentMainBinding
import org.json.JSONObject

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private var activityCallback: searchSpinnerListener? = null

    interface searchSpinnerListener {
        fun pokemonSelect(text: JSONObject)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.fetchPokemonNames() // fetch the pokemon names when the fragment is created
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filteredPokemonNames.observe(viewLifecycleOwner, { pokemonNames ->
            binding.spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, pokemonNames)
        })

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // pass the selected pokemon to the activity
                val selectedPokemonName = binding.spinner.selectedItem.toString()
                val selectedPokemonJsonObject = JSONObject()
                selectedPokemonJsonObject.put("name", selectedPokemonName)

                val infoFragment = InfoFragment()
                val bundle = Bundle()
                bundle.putString("selectedPokemonName", selectedPokemonName)
                infoFragment.arguments = bundle

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.infoFragView, infoFragment)
                    .addToBackStack(null)
                    .commit()

                activityCallback?.pokemonSelect(selectedPokemonJsonObject)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}


