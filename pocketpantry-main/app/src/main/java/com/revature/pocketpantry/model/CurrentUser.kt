package com.revature.pocketpantry.model

import android.util.Log
import com.revature.pocketpantry.network.SpoonApi
import com.revature.pocketpantry.pantry.*
import kotlinx.coroutines.*

object CurrentUser {

    var savedRecipeIDs = mutableListOf<Int>()
    var shoppingList = mutableListOf<Int>()
    var name:String="Mrs Tester"
    var password:String=""
    var uuid: String = "000000001"
    var appliances: ArrayList<Int> = ArrayList<Int>()
    var supplies: MutableList<Triple<Int, Int, String>> = mutableListOf<Triple<Int, Int, String>>()
    var staples: MutableList<Triple<Int, Int, String>> = ArrayList<Triple<Int, Int, String>>()
    var preferences = mutableListOf<String>()
    var diet :String? = null
    var savedRecipes = mutableListOf<Recipe>()
    var groceryListIDs = mutableListOf<Int>()
 //   @Transient
    var groceryList = mutableListOf<Ingredient>()


    init {
        //dummyPantry.forEach { addToPantry(it.id) }
        //dummyStaples.forEach { addToStaples(it.id) }
    }

    fun loadRecipes(){
       // val recipes = listOf<Int>(665172)
     //   savedRecipeIDs.addAll(recipes)
    }

    fun clear()
    {
        savedRecipes = mutableListOf<Recipe>()
        savedRecipeIDs = mutableListOf<Int>()
        shoppingList = mutableListOf<Int>()
        name=""
        password=""
        uuid= "-1"
        appliances= ArrayList<Int>()
        supplies= mutableListOf<Triple<Int, Int, String>>()
        staples= ArrayList<Triple<Int, Int, String>>()
        preferences= mutableListOf()
        diet = null
        groceryListIDs = mutableListOf<Int>()
    }


    /**
     * adds a recipe to the saved recipe list if it does not contain it
     * @param recipe - The recipe that the user wants to add
     */
    fun addToSavedRecipeList(recipe: Recipe) : Boolean{
        if (savedRecipeIDs.contains(recipe.id)){
            return false
        }
        return savedRecipeIDs.add(recipe.id)
    }

    /**
     * adds a recipe to the shopping list if it does not contain it
     * @param recipe - The recipe that the user wants to add
     */
    fun addToShoppingList(recipe: Recipe): Boolean{
        if (shoppingList.contains(recipe.id))
            return false

        recipe.extendedIngredients.forEach { ingredient ->
            MainScope().launch {
                ingredient.id?.let { id ->
                    groceryListIDs.add(id)
                    groceryList.add(SpoonApi.retrofitService.getIngredient(id))
                }
            }
        }
        return shoppingList.add(recipe.id)
    }

    /**
     *removes a recipe from the saved recipes list
     * @param recipe - The recipe that the user wants to remove
     */
    fun removeFromSavedRecipeList(recipe: Recipe): Boolean{
        if (!savedRecipeIDs.contains(recipe.id))
            return false

        return savedRecipeIDs.remove(recipe.id)
    }

    /**
     * This function makes sure that the shopping list contains the id that we are trying to delete.
     * @param recipe - The recipe that the user wants to remove
     */
    fun removeFromShoppingList(recipe: Recipe) : Boolean{
        if (!shoppingList.contains(recipe.id))
            return false

        recipe.extendedIngredients.forEach { ingredient ->
            ingredient.id?.let { id ->
                groceryListIDs.removeAll { it == id }
                groceryList.removeAll { it.id == id }
            }
        }
        return shoppingList.remove(recipe.id)
    }

    fun addToGroceryList(ingredient: Ingredient) =
        if (groceryList.contains(ingredient))
            false
        else {
            Log.i("GroceryList", "ID: ${ingredient.id}")
            groceryList.add(ingredient)
            groceryListIDs.add(ingredient.id)
        }


    fun removeFromGroceryList(ingredient: Ingredient) =
        if (!groceryList.contains(ingredient))
            false
        else {
            groceryList.removeAll { ingredient.id == it.id }
            groceryListIDs.removeAll { it == ingredient.id }
        }

    fun addToPantry(id: Int): Boolean {
        supplies.forEach { if(it.first == id) return false}
        staples.forEach { if(it.first == id) return false}
        return supplies.add(Triple(id, 0, "cups"))
    }

    fun addToStaples(id: Int): Boolean {
        staples.forEach { if(it.first == id) return false}
        supplies.forEach { if(it.first == id) return false}
        return staples.add(Triple(id, 0, "cups"))
    }

    fun removeFromPantry(id: Int): Boolean = supplies.removeIf { it.first == id }

    fun removeFromStaples(id: Int): Boolean = staples.removeIf { it.first == id }

    fun moveGroceryToStaples(id: Int) = removeFromPantry(id) && addToStaples(id)

    fun moveStapleToGroceries(id: Int) = removeFromStaples(id) && addToPantry(id)

    fun List<Triple<Int, Int, String>>.containsIngredient(id: Int): Boolean {
        this.forEach { if(it.first == id) return true }
        return false
    }
}

val tag = "testing"