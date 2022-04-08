package com.revature.pocketpantry.homesearch


import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.*
import androidx.recyclerview.widget.*

import com.revature.pocketpantry.databinding.HomeSearchGridViewItemBinding
import com.revature.pocketpantry.model.RecipeSearchItem


class HomeSearchAdapter(val listener:CustomHomeSearchClickListener):
    ListAdapter<RecipeSearchItem, HomeSearchAdapter.HomeSearchViewHolder>(DiffCallback) {

    inner class HomeSearchViewHolder( var binding: HomeSearchGridViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipeSearchItem) {
            binding.hsRecipe = recipe
            binding.executePendingBindings()
        }

    }

    // Construct RecyclerView.ViewHolder and set the view to display content
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeSearchAdapter.HomeSearchViewHolder {
        return HomeSearchViewHolder(HomeSearchGridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    // used to fetch data and fill the view holder's layout
    override fun onBindViewHolder(holder: HomeSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.root.layoutParams = RecyclerView.LayoutParams(
            MATCH_PARENT,
            WRAP_CONTENT
        ).apply { setMargins(16, 16, 16, 16) }

        holder.itemView.setOnClickListener {
            listener.onClick(getItem(position))
        }
    }


    companion object DiffCallback: DiffUtil.ItemCallback<RecipeSearchItem>() {
        override fun areItemsTheSame(oldItem: RecipeSearchItem, newItem: RecipeSearchItem): Boolean {
            return oldItem.title === newItem.title
        }

        override fun areContentsTheSame(oldItem: RecipeSearchItem, newItem: RecipeSearchItem): Boolean {
            return oldItem== newItem
        }
    }

    class CustomHomeSearchClickListener(val clickListener : (RecipeSearchItem) -> Unit){
        fun onClick(recipe: RecipeSearchItem) = clickListener(recipe)
    }
}




