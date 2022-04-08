package com.revature.pocketpantry.util

import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.*
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.*
import com.revature.pocketpantry.databinding.ShoppingListItemBinding
import com.revature.pocketpantry.model.*
import com.revature.pocketpantry.pantry.PantryAdapter
import com.revature.pocketpantry.shoppingList.ShoppingList

abstract class IngredientAdapter<T: AbstractIngredient, VH: ViewHolder>:
    ListAdapter<AbstractIngredient, VH>(DiffCallback) {

    fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.binding.root.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            .apply { setMargins(16, 8, 16, 8) }
        holder.bind(getItem(position))
        holder.binding.executePendingBindings()
    }

    companion object DiffCallback: DiffUtil.ItemCallback<AbstractIngredient>() {
        override fun areItemsTheSame(oldItem: AbstractIngredient, newItem: AbstractIngredient) = oldItem === newItem

        override fun areContentsTheSame(oldItem: AbstractIngredient, newItem: AbstractIngredient) = oldItem.id == newItem.id
    }

    abstract class IngredientViewHolder(open var binding: ViewDataBinding): ViewHolder(binding.root) {
        abstract fun bind(ingredient: AbstractIngredient)
    }

}