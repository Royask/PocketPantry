package com.revature.pocketpantry.selectedRecipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revature.pocketpantry.model.Recipe
import com.revature.pocketpantry.network.SpoonApi
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * This is the selected Recipe View Model that sends an http request to spoonacular to get
 * the recipe information for the passed in recipe.
 *
 * @author Jacob Ginn
 */
class SelectedRecipeViewModel(private val recipeID: Int) : ViewModel() {

    /** This is the selected recipe that the user has selected*/
    private val _selectedRecipe =  MutableLiveData<Recipe>()
    val selectedRecipe : LiveData<Recipe>
        get() = _selectedRecipe

    /** is it in the favorites */
    var isFavorite = false

    /** is it in the shopping list*/
    var isAddedToCart = false

    init {
        getSelectedRecipe()
    }


    /**
     * sends an http request through retrofit to get the recipe information
     */
    fun getSelectedRecipe(){
        viewModelScope.launch {
            try {
                _selectedRecipe.value = SpoonApi.retrofitService.getRecipe(recipeID)
                println("Getting the selected recipe: ${_selectedRecipe.value}")
            }catch (e : Exception){
                println("Error retrieving Recipe Please Try Again")
            }
        }
    }





}