package com.revature.pocketpantry.model

import com.revature.pocketpantry.network.SpoonApiService

/**
 * The backing abstract class inherited by all other Ingredient classes
 * @param id the ID of the Ingredient
 * @param name the name of Ingredient
 * @param image the relative url specifying the location of the photo of the Ingredient
 * @author Alec Ramirez
 */
abstract class AbstractIngredient (
    open val id: Int,
    open val name: String,
    open val image: String
)

/**
 * Object representation of an Ingredient. This is what Spoonacular returns when getting an
 * Ingredient by ID or when getting a list of Ingredients from a Recipe.
 * @param id the ID of the Ingredient
 * @param name the name of Ingredient
 * @param image the relative url specifying the location of the photo of the Ingredient
 * @property imageUrl the absolute url specifying the location of the photo of the Ingredient
 * @author Alec Ramirez
 */
data class Ingredient(
    override val name: String,
    override val id: Int,
    val original: String,
    val originalName: String,
    val possibleUnits: List<String>,
    val consistency: String,
    val aisle: String,
    override val image: String,
    val categoryPath: List<String>,
    val meta: List<String>
): AbstractIngredient(id, name, image){
    val imageUrl = "https://spoonacular.com/cdn/ingredients_100x100/$image"
}

/**
 * Abbreviated representation of an Ingredient. This is what Spoonacular returns when performing an
 * ingredient search
 * @param id the ID of the Ingredient
 * @param name the name of Ingredient
 * @param image the relative url specifying the location of the photo of the Ingredient
 * @property imageUrl the absolute url specifying the location of the photo of the Ingredient
 * @author Alec Ramirez
 */
data class IngredientResult(
    override val id: Int,
    override val name: String,
    override val image: String
): AbstractIngredient(id, name, image){
    val imageUrl = "https://spoonacular.com/cdn/ingredients_100x100/$image"
}

/** Wrapper for List of IngredientResults. This is what Spoonacular returns when performing an
 * Ingredient search
 * @param results List of IngredientResults matching the search criteria in SpoonApiService.getIngredient
 * @param number  number of results returned by the search.
 * @param totalResults total number of available results. This will be equal to number if number
 *  isn't specified in the http request
 * @see SpoonApiService.getIngredient
 * @author Alec Ramirez
 */
data class IngredientSearch(
    val results: MutableList<IngredientResult>,
    val number: Int,
    val totalResults: Int
)