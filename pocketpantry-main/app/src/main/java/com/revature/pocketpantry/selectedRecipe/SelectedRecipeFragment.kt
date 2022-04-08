package com.revature.pocketpantry.selectedRecipe

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.revature.pocketpantry.R
import com.revature.pocketpantry.databinding.FragmentSelectedRecipeBinding
import com.revature.pocketpantry.network.UserDatabaseController
import com.revature.pocketpantry.savedRecipes.SavedRecipeViewModel
import kotlinx.coroutines.selects.select


/**
 * This is the selected Recipe fragment that contains all of the functionality for adding to the
 * favorites and the shopping list in the menu bar.
 *
 * @author Jacob Ginn
 */
class SelectedRecipeFragment : Fragment() {

    /** The view model that contains all of the recipe data that is being displayed to the user*/
    private lateinit var selectedRecipeViewModel : SelectedRecipeViewModel

    /** The Saved recipe view model so that we can add and remove from the users lists*/
    private val savedRecipeVM : SavedRecipeViewModel by activityViewModels()

    /** The safe args that are passed to this fragment */
    private val args : SelectedRecipeFragmentArgs by navArgs()


    /**
     * This is called when the fragment is created and sets up all of the viewmodels and the bindings
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val binding =  FragmentSelectedRecipeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        //TODO: This is where I would take the safe args and get the id of recipe that is being viewed
        //sets up the viewModelFactory and passes the id of the recipe to the view model factor
        //getting the recipe ID through safe args through navigation
        val viewModelFactory = SelectedRecipeViewModelFactory(args.recipeId)
        selectedRecipeViewModel = ViewModelProvider(this,viewModelFactory)
            .get(SelectedRecipeViewModel::class.java)

        binding.viewModel = selectedRecipeViewModel


        return binding.root
    }

    /**
     * This is where the menu options in the top left are created. When they are created we need to
     * check if they are in the currentUsers shopping list or one of their favorite recipes
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.recipe_menu,menu)

        if (savedRecipeVM.isInFavorites(args.recipeId)){
            val favoriteItem = menu.findItem(R.id.menu_save_recipe)

            selectedRecipeViewModel.isFavorite = true
            favoriteItem.setIcon(R.drawable.ic_baseline_favorite_24)
        }
        if (savedRecipeVM.isInShoppingList(args.recipeId)){
            val shoppingItem = menu.findItem(R.id.menu_save_shopping_list)

            selectedRecipeViewModel.isAddedToCart = true
            shoppingItem.setIcon(R.drawable.ic_baseline_remove_shopping_cart_24)
        }
    }

    /**
     * This is called when a menu option is selected.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){

            //save or remove from favorite
            R.id.menu_save_recipe -> {
                addOrDeleteFavoriteRecipe(item)
                true
            }

            //save or remove from shopping list
            R.id.menu_save_shopping_list-> {
                addOrDeleteShoppingListRecipe(item)
                true
            }

            //navigate to the shopping cart
            R.id.menu_view_shopping_cart->{
                findNavController().navigate(R.id.action_selectedRecipeFragment_to_navigation_shoppingList)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * This function either adds or deletes from the favorite recipes depending on if it is already
     * contained in the list.
     */
    private fun addOrDeleteFavoriteRecipe(item: MenuItem){
        selectedRecipeViewModel.selectedRecipe.value?.let {recipe ->
            //contained need to delete
            if (selectedRecipeViewModel.isFavorite) {
                savedRecipeVM.removeFromSavedRecipes(recipe)
                selectedRecipeViewModel.isFavorite = false
                item.setIcon(R.drawable.ic_baseline_favorite_border_24)
                Toast.makeText(context, "Recipe removed from Favorites", Toast.LENGTH_SHORT).show()
            }
            //not contained need to add
            else{
                savedRecipeVM.addToSavedRecipes(recipe)
                selectedRecipeViewModel.isFavorite = true
                item.setIcon(R.drawable.ic_baseline_favorite_24)
                Toast.makeText(context, "Recipe added to Favorites", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun addOrDeleteShoppingListRecipe(item: MenuItem){
        selectedRecipeViewModel.selectedRecipe.value?.let {recipe ->
            //contained need to remove
            if (selectedRecipeViewModel.isAddedToCart){
                savedRecipeVM.removeFromShoppingListRecipes(recipe)
                selectedRecipeViewModel.isAddedToCart = false
                item.setIcon(R.drawable.ic_baseline_add_shopping_cart_24)
                Toast.makeText(context, "Recipe removed from Shopping List", Toast.LENGTH_SHORT).show()

            }
            //not contained need to add
            else{
                savedRecipeVM.addToShoppingListRecipes(recipe)
                selectedRecipeViewModel.isAddedToCart = true
                item.setIcon(R.drawable.ic_baseline_remove_shopping_cart_24)
                Toast.makeText(context, "Recipe added to Shopping List", Toast.LENGTH_SHORT).show()

            }
        }

    }
    override fun onPause() {
        UserDatabaseController.update()
        super.onPause()
    }
}