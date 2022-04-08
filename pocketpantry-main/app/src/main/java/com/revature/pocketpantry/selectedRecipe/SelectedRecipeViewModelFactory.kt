package com.revature.pocketpantry.selectedRecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

/**
 *
 * @author Jacob Ginn
 */
class SelectedRecipeViewModelFactory(
    private val recipeID : Int) : ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectedRecipeViewModel::class.java)){
            return SelectedRecipeViewModel(recipeID) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}