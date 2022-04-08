package com.revature.pocketpantry.shoppingList

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.*
import com.revature.pocketpantry.databinding.ShoppingListFragmentBinding
import com.revature.pocketpantry.model.*
import com.revature.pocketpantry.network.UserDatabaseController
import com.revature.pocketpantry.util.*

/**
 * This is the ShoppingList Fragment class. This is where the user will be able to view the items
 * in their shopping cart as well as check items off and delete them from the list entirely.  It
 * extends SearchableFragment in order to create an overlaid search menu on top of the root View
 * to search for Ingredients to add to the ShoppingList
 * @author Alec Ramirez
 */
class ShoppingList: SearchableFragment() {

    // Lazily retrieve the ShoppingListViewModel
    override val viewModel: ShoppingListViewModel by viewModels()
    // The ShoppingListFragmentBinding that will create a reference for all the Views in the Fragment
    private lateinit var binding: ShoppingListFragmentBinding

    /**
     * Here we override the parent onCreateView() and return the root View of a
     * ShoppingListFragmentBinding object inflated from the inflater.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ShoppingListFragmentBinding.inflate(inflater).run {

        // Set the binding object for the Fragment in case we need it later
        binding = this

        // Set LifecycleOwner for the binding
        lifecycleOwner = this@ShoppingList

        // Set the DataBinding variable viewModel:ShoppingListViewModel
        viewModel = this@ShoppingList.viewModel

        // Create adapters for all the recyclers
        rvShop.adapter    = GroceryAdapter()
        rvGrocerySearch.adapter  = IngredientSearchAdapter(this@ShoppingList)

        // Attach a swipe helper to the RecyclerView to enable swipe-to-delete functionality
        SwipeHelper(rvShop).attach()

        // This sets the OnClickListener of the ADD button to reveal the search menu to the user
        btnAddToList.setOnClickListener { startSearch() }

        root
    }

    /**
     * Called when user selects a grocery search item to add to the ShoppingList.  Adds the item
     * to the ViewModel and hides the search menu.
     */
    override fun addGrocery(id: Int) {
        viewModel.addGrocery(id)
        binding.llGrocerySearch.visibility = View.GONE
    }

    /**
     * Called by clicking the add button. This displays the search menu, expands the SearchView and
     * gives it focus (displaying the soft keyboard in the process) and attaches a custom
     * OnQueryTextListener (inherited from SearchableFragment) to the SearchView.
     */
    private fun startSearch() {
        binding.llGrocerySearch.visibility = View.VISIBLE
        binding.svGrocerySearch.isIconified = false
        binding.llGrocerySearch.setOnClickListener { it.visibility = View.GONE }
        binding.svGrocerySearch.setOnQueryTextListener(QueryTextListener(binding.rvGrocerySearch))
    }

    /** Updates the Firebase server any time the Application calls onPause() for this Fragment. */
    override fun onPause() {
        UserDatabaseController.update()
        super.onPause()
    }
}
