package com.revature.pocketpantry

import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.revature.pocketpantry.homesearch.HomeSearchAdapter
import com.revature.pocketpantry.model.Ingredient
import com.revature.pocketpantry.model.IngredientSearch
import com.revature.pocketpantry.model.Recipe

import com.revature.pocketpantry.model.*
import com.revature.pocketpantry.pantry.PantryAdapter
import com.revature.pocketpantry.savedRecipes.SavedRecipesAdapter
import com.revature.pocketpantry.shoppingList.GroceryAdapter
import com.revature.pocketpantry.util.IngredientSearchAdapter

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imageUrl: String?) {

    imageUrl?.let {
        val imageUri = it.toUri().buildUpon().scheme("https").build()

        Glide.with(imageView.context).load(imageUri)         //TODO .apply(RequestOptions().placeholder(R.drawable.loading_animation).error(R.drawable.ic_broken_image))
            .into(imageView)
    }
}

@BindingAdapter("groceries")
fun bindGroceryRecycler(recyclerView: RecyclerView, groceries: List<Ingredient>?) {
    val adapter = recyclerView.adapter as GroceryAdapter
    adapter.submitList(groceries)
}

@BindingAdapter("pantryItems")
fun bindPantryRecycler(recyclerView: RecyclerView, groceries: List<Ingredient>?) {
    val adapter = recyclerView.adapter as PantryAdapter
    adapter.submitList(groceries)
}

/**
 * This function will bind a list of recipes to a recycler view.
 */
@BindingAdapter("savedRecipes")
fun bindSavedRecipesRecycler(recyclerView: RecyclerView, recipes: List<Recipe>?) {
    val adapter = recyclerView.adapter as SavedRecipesAdapter
    adapter.submitList(recipes)
    Log.d("BindingAdapterListData","Binding List to the RecyclerView")
}


/**
 * This function will accept a recipe object and set the text in the text view to the
 * appropriate recipe instructions
 */
@BindingAdapter("instructions")
fun bindInstructions(textView: TextView, recipe : Recipe?){
    if (recipe?.instructions?.isEmpty() == true  || recipe == null){
        textView.text = "Sorry no instructions were found"
    }else{
        textView.text = Html.fromHtml(recipe.instructions, 0).toString()
    }

}

/**
 * This function will accept a recipe object and set the text in the text view to the
 * appropriate recipe ingredients
 */
@BindingAdapter("ingredients")
fun bindIngredients(textView: TextView, recipe : Recipe?){
    val stringBuilder = StringBuilder()
    if (recipe?.extendedIngredients?.isEmpty() == true || recipe == null){
        textView.text = "Sorry no Ingredients were found"
    }else{
        recipe.extendedIngredients.forEach { recipeIngredient ->
            stringBuilder.append(recipeIngredient.original+ "\n")
        }
        textView.text = stringBuilder
    }
}

@BindingAdapter("homeSearchGrid")
fun bindHomeSearch(recyclerView: RecyclerView, data:List<RecipeSearchItem>?) {
    val  adapter=recyclerView.adapter as HomeSearchAdapter
    adapter.submitList(data)
    adapter.notifyDataSetChanged()
}

@BindingAdapter("results")
fun bindSearchRecycler(recyclerView: RecyclerView, results: List<IngredientResult>?) {
    val adapter = recyclerView.adapter as IngredientSearchAdapter
    adapter.submitList(results)
    Log.d("BindingAdapterListData","Binding List to the RecyclerView")
}