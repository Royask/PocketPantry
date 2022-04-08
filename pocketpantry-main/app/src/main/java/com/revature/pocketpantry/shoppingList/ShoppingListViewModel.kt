package com.revature.pocketpantry.shoppingList

import androidx.lifecycle.*
import com.revature.pocketpantry.model.*
import com.revature.pocketpantry.util.SearchableViewModel
import kotlinx.coroutines.launch

/**
 * This is the ViewModel that holds all the LiveData for the ShoppingList Fragment.  It extends
 * SearchableViewModel() to inherit its two search functions as well as the LiveData for the
 * search results.
 * @author Alec Ramirez
 */
class ShoppingListViewModel: SearchableViewModel() {

    /** This is the LiveData that updates and contains the data for the ShoppingList RecyclerView */
    private val _groceries = MutableLiveData<MutableList<Ingredient>>()
    val groceries: LiveData<MutableList<Ingredient>>
        get() = _groceries.apply { value?.sortBy { it.aisle } }

    /** Initialize the value of the LiveData to reflect the data stored in the remote Database */
    init { getList() }

    /** Updates the LiveData to reflect the data in the model */
    private fun getList() { _groceries.value = CurrentUser.groceryList }

    /**
     * Adds the Dngredient to the ShoppingList and updates the LiveData
     * @param ingredient the Ingredient to be added to the ShoppingList
     */
    fun addGrocery(ingredient: Ingredient) {
        CurrentUser.addToGroceryList(ingredient)
        getList()
    }

    /**
     * Gets the Ingredient by its ID from the database, adds it to the model, and updates the LiveData.
     * @param id the ID of the Ingredient to be added to the ShoppingList
     */
    fun addGrocery(id: Int) { viewModelScope.launch { addGrocery(retro.getIngredient(id)) } }

    /**
     * Removes the Ingredient from the model and updates the LiveData
     * @param ingredient the Ingredient to be removed
     */
    fun removeGrocery(ingredient: Ingredient) {
        CurrentUser.removeFromGroceryList(ingredient)
        getList()
    }
}