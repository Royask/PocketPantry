package com.revature.pocketpantry.model

/**
 * This is the recipe object that contains the information for the recipes.
 * @author Jacob Ginn
 */
data class Recipe(
    val id: Int,
    val title: String,
    val image: String? = "https://spoonacular.com/cdn/ingredients_100x100/no.png",
    val instructions: String,
    val extendedIngredients: List<RecipeIngredient> = listOf(),
)

/**
 *This is the recipe ingredient that is displayed on the selected recipe screen
 * @author Jacob Ginn
 */
data class RecipeIngredient(
    val id: Int?,
    val original: String
)

data class RecipeSearchItem(var id: Int=-1, var image:String="", var title:String="ERROR")

data class RecipeSearch(var results: List<RecipeSearchItem> = listOf())

data class RandomRecipes(var recipes: List<RecipeSearchItem> =listOf())