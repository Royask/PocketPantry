package com.revature.pocketpantry.savedRecipes

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.revature.pocketpantry.databinding.SavedRecipeItemBinding
import com.revature.pocketpantry.model.Recipe

/**
 * This is the adapter that will contain the recycler views in the saved recipes fragment
 * @param customClickListener - the implementation of the click listener that needs to be put on
 *                              each recycler view item
 * @param customLongClickListener - the implementation of the long click listener that needs to be put on each
 *                                  recycler view item
 * @author Jacob Ginn
 */
class SavedRecipesAdapter(
    private val customClickListener:CustomClickListener,
    private val customLongClickListener: CustomLongClickListener)
    : ListAdapter<Recipe,SavedRecipesAdapter.RecipeViewHolder>(DiffCallback) {


    private val TAG =  "SavedRecipeAdapter"


    /**
     * This is the view holder for each of the recipes that are contained in a view in the recycler
     * view.
     */
    class RecipeViewHolder(private var binding: SavedRecipeItemBinding):
            RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe){
            binding.recipe = recipe

            //forces the data binding to execute immediately
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the list of recipes
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * creates a view holder for each of the items in the recycler view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(SavedRecipeItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * binds the data to each of the viewholders in the recycler view and sets the
     * click listeners for each of the different views
     */
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)

        holder.itemView.setOnClickListener{
            customClickListener.onClick(recipe)
        }

        holder.itemView.setOnLongClickListener{
            Log.d(TAG, "We are long Clicking")
            customLongClickListener.onLongClick(it, recipe)
            return@setOnLongClickListener true
        }
        holder.bind(recipe)
    }

    /**
     * a class that contains the lamda function that tells the view what to do on each click
     * event
     */
    class CustomClickListener(val clickListener : (Recipe) -> Unit){
        fun onClick(recipe: Recipe) = clickListener(recipe)
    }

    /**
     * a class that contains the lamda function that tells the view what to do on each long click
     * event
     */
    class CustomLongClickListener(val longClickListener: (View,Recipe) -> Unit){
        fun onLongClick(view:View, recipe: Recipe) = longClickListener(view,recipe)
    }
}