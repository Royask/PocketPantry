package com.revature.pocketpantry.pantry

import android.util.Log
import androidx.lifecycle.*
import com.revature.pocketpantry.model.*
import com.revature.pocketpantry.network.*
import com.revature.pocketpantry.util.SearchableViewModel
import kotlinx.coroutines.*

class PantryViewModel: SearchableViewModel() {

    private val _ingredientSearch = MutableLiveData<MutableList<IngredientResult>>()      //TODO make this private when not using dummy data
    val ingredientSearch: LiveData<MutableList<IngredientResult>>
        get() = _ingredientSearch

    val _groceries = MutableLiveData<MutableList<Ingredient>>()      //TODO make this private when not using dummy data
    val groceries: LiveData<MutableList<Ingredient>>
        get() = _groceries.apply { value?.sortBy { it.aisle } }

    val _staples = MutableLiveData<MutableList<Ingredient>>()      //TODO make this private when not using dummy data
    val staples: LiveData<MutableList<Ingredient>>
        get() = _staples.apply { value?.sortBy { it.aisle } }

    init {
        //getPantry()
    }

    fun addGrocery(ingredient: Ingredient): Boolean {
        if(CurrentUser.addToPantry(ingredient.id))
            _groceries.value?.let { return it.add(ingredient) }
        return false
    }

    fun addGrocery(id: Int) {
        viewModelScope.launch { addGrocery(retro.getIngredient(id)) }
    }

    fun removeGrocery(ingredient: Ingredient): Boolean {
        if(CurrentUser.removeFromPantry(ingredient.id))
            _groceries.value?.let { return it.remove(ingredient) }
        return false
    }

    fun addStaple(ingredient: Ingredient): Boolean {
        if(CurrentUser.addToStaples(ingredient.id))
            _staples.value?.let { return it.add(ingredient) }
        return false
    }

    fun removeStaple(ingredient: Ingredient): Boolean {
        if(CurrentUser.removeFromStaples(ingredient.id))
            _staples.value?.let { return it.remove(ingredient) }
        return true
    }

    fun moveGroceryToStaples(ingredient: Ingredient) =
         if (CurrentUser.moveGroceryToStaples(ingredient.id)) {
            _groceries.value?.remove(ingredient)
            _staples.value?.add(ingredient)
             true
        } else false
    
    fun moveStapleToGroceries(ingredient: Ingredient) =
        if(CurrentUser.moveStapleToGroceries(ingredient.id)) {
            _staples.value?.remove(ingredient)
            _groceries.value?.add(ingredient)
            true
        } else false

    fun moveStapleToPantry(ingredient: Ingredient) = CurrentUser.moveStapleToGroceries(ingredient.id)

    fun searchIngredients(search: String) = viewModelScope.async {
        retro.searchIngredients(search).apply { _ingredientSearch.value = this.results }}

    private fun getPantry() {

        //TODO get list of groceries and staples from firebase and feed them
        // to _groceries and _staples
        //val groceryIDs = firebasestuff
        //val stapleIDs = firesbasestuff

        val groceryIDs = listOf(15121, 6168, 11304, 20420, 11297, 10123, 1033, 11282, 11352, 99063, 11215, 5062)
        val stapleIDs  = listOf(2047, 1002030, 1022020, 2026, 6168, 1123, 4053, 1085, 11291, 20081, 1077, 10120129)

        viewModelScope.launch {
            _groceries.value = SpoonApi.getIngredients(groceryIDs)
            _staples.value = SpoonApi.getIngredients(stapleIDs)

            fun listToString(l: List<String>): String {
                var s = "listOf("
                l.forEach { s += "\"$it\", " }
                return "$s)"
            }

            fun printDummies() {
                var s = "\nval dummyPantry = listOf("
                groceries.value?.forEach { s += "\nIngredient(name=\"%s\", id=%s, original=\"%s\", originalName=\"%s\", possibleUnits=%s, consistency=\"%s\", aisle=\"%s\", image=\"%s\", categoryPath=%s, meta=%s),"
                    .format(it.name, it.id, it.original, it.originalName, listToString(it.possibleUnits), it.consistency, it.aisle, it.image, listToString(it.categoryPath), listToString(it.meta)) }
                s += ")\nval dummyStaples = listOf("

                staples.value?.forEach { s += "\nIngredient(name=\"%s\", id=%s, original=\"%s\", originalName=\"%s\", possibleUnits=%s, consistency=\"%s\", aisle=\"%s\", image=\"%s\", categoryPath=%s, meta=%s),"
                    .format(it.name, it.id, it.original, it.originalName, listToString(it.possibleUnits), it.consistency, it.aisle, it.image, listToString(it.categoryPath), listToString(it.meta)) }
                s += ")\n\n"

                println(s)
            }

            printDummies()
        }
    }


    // TODO after you go shopping, add all your checked groceries to pantry

    //fun addGrocery(id: Int): Deferred<Ingredient> =
    //    viewModelScope.async { retro.getIngredient(id).apply { _groceries.value?.add(this) }}


    //fun removeGrocery(id: Int):Deferred<Ingredient> =
    //    viewModelScope.async { retro.getIngredient(id).apply { _groceries.value?.remove(this) }}

}