//TEAM MEMBERS
//ALI HAZIME
//FATIMA KOURANI
//ZACHARY FAYBIK

package com.example.pokedexapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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

        //grabs pokemon names from view model
        viewModel.fetchPokemonNames()
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



        //spinner for dropdown list to select pokemon
        val defaultItem = "Select a Pokemon"
        viewModel.filteredPokemonNames.observe(viewLifecycleOwner) { pokemonNames ->
            binding.spinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                pokemonNames.toMutableList().apply { add(0, defaultItem) })
                .apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
        }

        //starts spinner selection at null so no pokemon will be selected
        binding.spinner.setSelection(0)

        //loads spinner with pokemon name
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                val selectedPokemonName = binding.spinner.selectedItem.toString()
                val selectedPokemonJsonObject = JSONObject()
                selectedPokemonJsonObject.put("name", selectedPokemonName)

                val infoFragment = InfoFragment()
                val bundle = Bundle()

                // passes selected pokemon using selectedpokemonname
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



