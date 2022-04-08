package com.revature.pocketpantry.savedRecipes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.revature.pocketpantry.R
import com.revature.pocketpantry.databinding.FragmentSavedRecipeBinding
import com.revature.pocketpantry.network.UserDatabaseController

/**
 * This is the saved recipes fragment that contains all of the data for the users lists
 * @author Jacob Ginn
 */
class SavedRecipesFragment : Fragment() {

    /** this is the debug tag */
    private val DEBUG_TAG = "SavedRecipeFragmentDebug"

    /** the data binding for the fragment */
    private lateinit var binding: FragmentSavedRecipeBinding

    /** the saved recipe view model that holds the data for the users recipes */
    private val viewModel: SavedRecipeViewModel by activityViewModels()

    /** the recycler adapter for the favorites recipes*/
    private lateinit var savedRecipeRecyclerAdapter: SavedRecipesAdapter

    /** the recycler adapter for the shopping list recipes */
    private lateinit var shoppingListRecyclerAdapter: SavedRecipesAdapter

    /**
     * When the fragment is created it will setup the data binding nand setup the proper adapters
     * and observers for the fragments.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSavedRecipeBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        val customClickListener = SavedRecipesAdapter.CustomClickListener{
            viewModel.viewSelectedRecipe(it)
        }

        //sets the long click listener for the favorite items
        val favoriteLongClick = SavedRecipesAdapter.CustomLongClickListener{ view , recipe->
            val popupMenu = PopupMenu(requireContext(),view)

            popupMenu.inflate(R.menu.favorite_item_context_menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.remove_from_favorites-> {
                        viewModel.removeFromSavedRecipes(recipe)
                        true
                    }
                    R.id.add_to_shopping_list->{
                        viewModel.addToShoppingListRecipes(recipe)
                        true
                    }
                    else-> false
                }
            }
            popupMenu.show()
        }

        //sets the long click listener for the shopping list items
        val shoppingListLongClick = SavedRecipesAdapter.CustomLongClickListener{view, recipe ->
            val popupMenu = PopupMenu(requireContext(),view)

            popupMenu.inflate(R.menu.shopping_list_item_context_menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.remove_from_shopping_list-> {
                        viewModel.removeFromShoppingListRecipes(recipe)
                        true
                    }
                    R.id.add_to_favorites->{
                        viewModel.addToSavedRecipes(recipe)
                        true
                    }
                    else-> false
                }
            }
            popupMenu.show()
        }

        savedRecipeRecyclerAdapter = SavedRecipesAdapter(customClickListener, favoriteLongClick)

        shoppingListRecyclerAdapter = SavedRecipesAdapter(customClickListener, shoppingListLongClick)

        //The adapter for the saved recipes
        binding.savedRecipeRecycler.adapter = savedRecipeRecyclerAdapter

        //the adapter for the shopping list
        binding.shoppingListRecipeRecycler.adapter = shoppingListRecyclerAdapter

        setupObservers()
        // Inflate the layout for this fragment
        return binding.root
    }

    /**
     * When the options menu is created it will setup the search view and the rest of the menu items
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.saved_recipe_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)

        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredRecipeList = newText?.let { viewModel.filteredRecipeList(it) }
                savedRecipeRecyclerAdapter.apply {
                    submitList(filteredRecipeList)
                    notifyDataSetChanged()
                }

                val filteredShoppingList = newText?.let { viewModel.filteredShoppingList(it) }
                shoppingListRecyclerAdapter.apply {
                    submitList(filteredShoppingList)
                    notifyDataSetChanged()
                }
                return true
            }

        })

    }

    /**
     * sets up the observers for the livedata in the view model for the onclick
     * and when the data in the lists are changed.
     */
    private fun setupObservers(){

        //observer to know when to navigate to the selected recipe screen
        viewModel.navigateToDetails.observe(viewLifecycleOwner, Observer {
            it.getRecipeIfNotHandled()?.let { recipe ->
                Log.d(DEBUG_TAG, "ID of recipe : ${recipe.id.toString()}")
                val action = SavedRecipesFragmentDirections.actionNavigationSavedRecipesToSelectedRecipeFragment(recipe.id)
                findNavController().navigate(action)
            }
        })

        //observer to know when the saved recipe list is changed
        viewModel.savedRecipeList.observe(viewLifecycleOwner, Observer {
            Log.d(DEBUG_TAG, "We are getting a new list when it is observed")
            if (it.isEmpty()){
                binding.tvEmptyFavorites.visibility = View.VISIBLE
            }else{
                binding.tvEmptyFavorites.visibility = View.GONE
            }
            savedRecipeRecyclerAdapter.submitList(it)
            savedRecipeRecyclerAdapter.notifyDataSetChanged()
        })

        //observer to know when the shopping list is changed
        viewModel.shoppingList.observe(viewLifecycleOwner, Observer {
            Log.d(DEBUG_TAG, "We are getting a new list when it is observed")
            if (it.isEmpty()){
                binding.tvEmptyShoppingList.visibility = View.VISIBLE
            }else{
                binding.tvEmptyShoppingList.visibility = View.GONE
            }
            shoppingListRecyclerAdapter.submitList(it)
            shoppingListRecyclerAdapter.notifyDataSetChanged()
        })
    }


    override fun onPause() {
        UserDatabaseController.update()
        super.onPause()
    }

}