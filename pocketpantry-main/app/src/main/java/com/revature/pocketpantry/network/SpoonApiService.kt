package com.revature.pocketpantry.network

import android.util.Log
import com.revature.pocketpantry.*
import com.revature.pocketpantry.model.*
import com.squareup.moshi.Moshi
import com.revature.pocketpantry.apiKey
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.lang.Exception

private const val apiKey = apiKey

private const val BASE_URL = "https://api.spoonacular.com/"
private val moshi = MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build())
private val retrofit = Retrofit.Builder().addConverterFactory(moshi).baseUrl(BASE_URL).build()

/**
 * Interface containing the function definitions for all the Spoonacular http requests. This will
 * be converted into an instantiable class using retrofit ConverterFactory.
 * @author Alec Ramirez
 */
interface SpoonApiService {

    /**
     * Retrieves a list of recipes from the Spoonacular database matching the given search criteria
     * @param dishName     the (natural language) search query
     * @param intolerances the list of Ingredients that must be excluded from the search results
     * @param diet         the diet for which the recipe must be suitable ie vegan, ketogenic, etc
     * @param instructionsRequired boolean value indicating whether a result must include instructions
     * @param key          the unique apiKey assigned by Spoonacular to authorize the http request
     */
    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @Query("query") dishName : String?=null,
        @Query("intolerances") intolerances:String?=null,
        @Query("diet") diet:String?=null,
        @Query("instructionsRequired")instructionsRequired:String = "true",
        @Query("apiKey") key:String= apiKey
    ): RecipeSearch

    /**
     * Retrieves a single Recipe using its ID
     * @param recipeId the ID of the Recipe
     * @param key      the unique apiKey assigned by Spoonacular to authorize the http request
     */
    @GET("recipes/{id}/information")
    suspend fun getRecipe(
        @Path("id") recipeId : Int,
        @Query("apiKey") key :String = apiKey
    ): Recipe

    /**
     * Retrieves a list of Ingredients matching the search query parameters
     * @param query  the partial or full name of the Ingredient
     * @param number the expected number of results (between 1 and 100)
     * @param key    the unique apiKey assigned by Spoonacular to authorize the http request
     */
    @GET("food/ingredients/search")
    suspend fun searchIngredients(
        @Query("query") query: String? = null,
        @Query("number") number: Int? = null,
        @Query("apiKey") key: String = apiKey
    ): IngredientSearch

    /**
     * Retrieves a list of Ingredients matching the search query parameters
     * @param query  the partial or full name of the Ingredient
     * @param number the expected number of results (between 1 and 100)
     * @param meta   boolean value indicating whether to include meta information
     * @param key    the unique apiKey assigned by Spoonacular to authorize the http request
     */
    @GET("food/ingredients/autocomplete")
    suspend fun autocompleteIngredients(
        @Query("query") query: String? = null,
        @Query("number") number: Int? = 25,
        @Query("metaInformation") meta: Boolean? = true,
        @Query("apiKey") key: String = apiKey
    ): MutableList<IngredientResult>

    /**
     * Retrieves a single Ingredient using its ID
     * @param id  the ID of the Ingredient
     * @param key the unique apiKey assigned by Spoonacular to authorize the http request
     */
    @GET("food/ingredients/{id}/information")
    suspend fun getIngredient(
        @Path("id") id: Int,
        @Query("apiKey") key: String = apiKey
    ): Ingredient

    /**
     * Retrieves a list of random Recipes
     * @param number the expected number of results (between 1 and 100)
     * @param key    the unique apiKey assigned by Spoonacular to authorize the http request
     */
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 50,
        @Query("apiKey") key: String = apiKey
    ): RandomRecipes
}

/** The singleton object holding a single instance of SpoonApiService */
object SpoonApi {

    /** Lazily load in an instance of SpoonApiService called retrofitService */
    val retrofitService: SpoonApiService by lazy {
        retrofit.create(SpoonApiService::class.java)
    }

    /**
     * Retrieves a list of Ingredients given a list of their associated IDs
     * @param ids List of ids used to retrieve the Ingredients
     */
    suspend fun getIngredients(ids: List<Int>): MutableList<Ingredient> = ArrayList<Ingredient>().apply {

        try {
            ids.forEach { add(retrofitService.getIngredient(it)) }
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Retrieves a list of recipes from the Spoonacular database matching the given search criteria
     * @param terms the (natural language) search query
     */
    suspend fun getThoseRecipes(terms:String):RecipeSearch{
        var temp:String = ""
        for(x in CurrentUser.preferences)
        {
            var y = x
            when(y)
            {
                "No Wheat"-> y ="Wheat"
                "Dairy Free"-> y ="Dairy"
                "No Egg"-> y ="Egg"
                "Gluten Free"-> y ="Gluten"
                "No Grain"-> y ="Grain"
                "No Peanuts"-> y ="Peanut"
                "No Seafood"-> y ="Seafood"
                "No Sesame"-> y ="Sesame"
                "No Shellfish"-> y ="Shellfish"
                "No Soy"-> y ="Soy"
                "Sulfite Free"-> y ="Sulfite"
                "No Tree Nuts"-> y ="Tree Nut"
                else-> Log.i("Conversion", "Intolerance not found")
            }
            temp+= "$y,"
        }
        temp = temp.substringBeforeLast(',')

        var diet:String? = CurrentUser.diet
        if(diet.equals("Vegetarian Only", true))
            diet = "Vegetarian"
        return retrofitService.getRecipes(dishName = terms, intolerances = temp, diet = diet)
    }
}
