package com.revature.pocketpantry.savedRecipes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revature.pocketpantry.model.Recipe
import com.revature.pocketpantry.model.CurrentUser
import com.revature.pocketpantry.model.RecipeIngredient
import com.revature.pocketpantry.network.SpoonApi
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

/**
 *This is the saved recipe view model that will contain all of the list data for the users and
 * the live data that is needed to update the list and navigate to the next screen.
 *
 * @author Jacob Ginn
 */
class SavedRecipeViewModel: ViewModel() {


    /** This is the saved recipe list */
    private val _savedRecipesList = MutableLiveData<MutableList<Recipe>>()
    val savedRecipeList : LiveData<MutableList<Recipe>>
        get() = _savedRecipesList


    /** This is the shopping list */
    private val _shoppingList = MutableLiveData<MutableList<Recipe>>()
    val shoppingList : LiveData<MutableList<Recipe>>
        get() = _shoppingList

    //This is for the navigation to the selected recipe if the user clicks on an item in one of their
    //list this will change this to the correct recipe and the fragment will observe it through
    // the liveData
    val _navigateToDetails = MutableLiveData<WrapperEvent<Recipe>>()
    val navigateToDetails : LiveData<WrapperEvent<Recipe>>
        get() = _navigateToDetails


    init {
        _savedRecipesList.value = mutableListOf()
        _shoppingList.value = mutableListOf()
        setupSavedList()
        setupShoppingList()
    }


    /**
     * this function gets each individual recipe from each of the ids that are contained in the
     * users saved Recipes
     */
    private fun setupSavedList() {
        viewModelScope.launch {
            try {
                val tempList = mutableListOf<Recipe>()
                CurrentUser.savedRecipeIDs.forEach {
                    tempList.add(SpoonApi.retrofitService.getRecipe(it))
                }
                if (tempList.isNotEmpty()){
                    _savedRecipesList.value = tempList
                }
            }catch (e : Exception){
                Log.d("savedRecipeSetup", "Spoonapi service is not available sorry for the " +
                        "inconvenience")
            }
        }
    }


    /**
     * this function gets each individual recipe from each of the ids that are contained in the
     * users shopping list
     */
    fun setupShoppingList(){
        viewModelScope.launch {
            val tempList = mutableListOf<Recipe>()
            try {
                CurrentUser.shoppingList.forEach {
                    tempList.add(SpoonApi.retrofitService.getRecipe(it.toInt()))
                }
                if (tempList.isNotEmpty()){
                    _shoppingList.value = tempList
                }
            }catch (e : Exception){
                Log.d("savedRecipeSetup", "Spoonapi service is not available sorry for the " +
                        "inconvenience")
            }
        }
    }

    /**
     * add the recipe to the favorites
     */
    fun addToSavedRecipes(recipe: Recipe){
        if (CurrentUser.addToSavedRecipeList(recipe)){
            _savedRecipesList.value?.add(recipe)
            _savedRecipesList.postValue(savedRecipeList.value)
        }
    }


    /**
     * add the recipe to the shopping list
     */
    fun addToShoppingListRecipes(recipe: Recipe){
        if (CurrentUser.addToShoppingList(recipe)){
            _shoppingList.value?.add(recipe)
            _shoppingList.postValue(shoppingList.value)
        }
    }

    /**
     * remove the recipe from the favorites
     */
    fun removeFromSavedRecipes(recipe: Recipe){
        if (CurrentUser.removeFromSavedRecipeList(recipe)){
            _savedRecipesList.value?.remove(recipe)
            _savedRecipesList.postValue(savedRecipeList.value)
        }
    }


    /**
     * remove the recipe from the shopping list
     */
    fun removeFromShoppingListRecipes(recipe: Recipe){
        if (CurrentUser.removeFromShoppingList(recipe)){
            _shoppingList.value?.remove(recipe)
            _shoppingList.postValue(shoppingList.value)
        }
    }

    /**
     * this function returns true if the id is in the favorites
     * false if it does not
     */
    fun isInFavorites(recipeId : Int) : Boolean{
        return CurrentUser.savedRecipeIDs.contains(recipeId)
    }

    /**
     * this function returns true if the id is in the shopping list
     * false if it does not
     */
    fun isInShoppingList(recipeId : Int) : Boolean{
        return CurrentUser.shoppingList.contains(recipeId)
    }

    /**
     * When a recipe is selected it will navigate to the next screen
     */
    fun viewSelectedRecipe(recipe: Recipe){
        _navigateToDetails.value = WrapperEvent(recipe)
    }

    /**
     * filters the recipes based on the search view window query
     */
    fun filteredRecipeList(queryText : String) : List<Recipe>{
        return _savedRecipesList.value!!.filter { recipe -> recipe.title.toLowerCase(Locale.ROOT)
            .contains(queryText.toLowerCase(Locale.ROOT)) }
    }

    /**
     * filters the recipes based on the search view window query
     */
    fun filteredShoppingList(queryText: String) : List<Recipe>{
        return _shoppingList.value!!.filter { recipe ->  recipe.title.toLowerCase(Locale.ROOT)
            .contains(queryText.toLowerCase(Locale.ROOT)) }
    }

}