package com.revature.pocketpantry.homesearch

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.revature.pocketpantry.databinding.FragmentHomeSearchBinding
import com.revature.pocketpantry.network.SpoonApi
import com.revature.pocketpantry.network.UserDatabaseController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *
 */
class HomeSearch : Fragment() {

    /** */
    private val DEBUG_TAG = "HomeSearchFragment"

    /** */
    private lateinit var homeSearchAdapter:HomeSearchAdapter

    /** */
    private val viewModel: HomeSearchViewModel by viewModels()

    /** */
    private lateinit var binding: FragmentHomeSearchBinding

    /**
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeSearchBinding.inflate(inflater)

        binding.homeViewModel = viewModel
        setupSearchBar()
        var customClick = HomeSearchAdapter.CustomHomeSearchClickListener{
            viewModel.navigateToSelectedRecipe(it)
        }
        homeSearchAdapter= HomeSearchAdapter(customClick)
        binding.photosGrid.adapter=homeSearchAdapter
        setupObservers()

        binding.searchView.isIconified = false

        return binding.root

    }

    /**
     *
     */
    private fun setupObservers(){
        binding.homeViewModel!!.homeSearchRecipes.observe(viewLifecycleOwner,{
            homeSearchAdapter.apply {
                submitList(it)
                notifyDataSetChanged()
            }
        })

        viewModel.navigateSelectedRecipe.observe(viewLifecycleOwner,{
            it.getRecipeIfNotHandled()?.let{ recipeSearchItem->
                Log.d(DEBUG_TAG, "ID of recipe : ${recipeSearchItem.id.toString()}")
                val action = HomeSearchDirections.actionNavigationHomeSearchToSelectedRecipeFragment(recipeSearchItem.id)
                findNavController().navigate(action)
            }
        })
    }

    /**
     *
     */
    private fun setupSearchBar(){
        binding.searchView.setOnQueryTextListener(object:
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    Log.d(DEBUG_TAG, "Sending a query of text $query")
                    viewModel.getSearchResult(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(DEBUG_TAG, "Sending a query of text $newText")

                return false
            }
        })
    }
    override fun onPause() {
        UserDatabaseController.update()
        super.onPause()
    }


}