package com.revature.pocketpantry.pantry

import android.view.*
import android.view.ViewGroup.LayoutParams.*
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.*
import com.revature.pocketpantry.databinding.PantryItemBinding
import com.revature.pocketpantry.model.*
import com.revature.pocketpantry.util.IngredientAdapter

class PantryAdapter: IngredientAdapter<Ingredient, PantryAdapter.GroceryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GroceryViewHolder(PantryItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        super.onBindViewHolder(holder as IngredientViewHolder, position)
    }

    class GroceryViewHolder(var pantryBinding: PantryItemBinding): IngredientViewHolder(pantryBinding) {
        override fun bind(ingredient: AbstractIngredient) {
            pantryBinding.grocery = ingredient as Ingredient
        }
    }
}