package com.revature.pocketpantry.homesearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.revature.pocketpantry.model.RecipeSearch
import com.revature.pocketpantry.model.RecipeSearchItem
import com.revature.pocketpantry.network.SpoonApi
import com.revature.pocketpantry.network.SpoonApi.getThoseRecipes
import com.revature.pocketpantry.network.SpoonApiService
import com.revature.pocketpantry.savedRecipes.WrapperEvent
import com.revature.pocketpantry.util.SearchableViewModel
import kotlinx.coroutines.launch
import java.sql.Wrapper

class HomeSearchViewModel : SearchableViewModel() {
    // the internal mutable instance of liveData is created to hold  Recipes
    private val _homeSearchRecipes = MutableLiveData<List<RecipeSearchItem>>()

    //the external immutable live data for recipes
    val homeSearchRecipes : LiveData<List<RecipeSearchItem>>
        get()=_homeSearchRecipes

    private val _navigateSelectedRecipe = MutableLiveData<WrapperEvent<RecipeSearchItem>>()

    val navigateSelectedRecipe : MutableLiveData<WrapperEvent<RecipeSearchItem>>
    get() = _navigateSelectedRecipe

    init {
        loadRandomRecipes()
    }

     fun getSearchResult(input:String) {
        viewModelScope.launch {

            try {

                val recipeSearch : RecipeSearch = getThoseRecipes(input)
                val tempSearchList = recipeSearch.results
                Log.d("HomeSearchVM", "we are getting : $tempSearchList")


                if(tempSearchList.isNotEmpty()){

                    _homeSearchRecipes.value=tempSearchList
                }else{
                    _homeSearchRecipes.value  = mutableListOf()
                }

            } catch (e: Exception) {

                Log.d("HomeSearchVM", "SpoonApi is not available at this time.")

            }
        }

    }
    private fun loadRandomRecipes() {
        try { viewModelScope.launch {
                _homeSearchRecipes.value= retro.getRandomRecipes(60).recipes
        }} catch (e: Exception) {
            e.printStackTrace()
            loadRandomRecipes()
        }
    }

    fun navigateToSelectedRecipe(item : RecipeSearchItem){
        _navigateSelectedRecipe.value  = WrapperEvent(item)
    }

}