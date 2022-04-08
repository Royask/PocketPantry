package com.revature.pocketpantry.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import com.revature.pocketpantry.databinding.IngredientSearchItemBinding
import com.revature.pocketpantry.databinding.PantryItemBinding
import com.revature.pocketpantry.databinding.SearchLayoutBinding
import com.revature.pocketpantry.model.*
import com.revature.pocketpantry.pantry.Pantry
import com.revature.pocketpantry.pantry.PantryAdapter

class IngredientSearchAdapter(val fragment: SearchableFragment)
    :IngredientAdapter<IngredientResult, IngredientSearchAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ResultViewHolder(IngredientSearchItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        super.onBindViewHolder(holder as IngredientViewHolder, position)
    }

    inner class ResultViewHolder(var resultBinding: IngredientSearchItemBinding):
        IngredientAdapter.IngredientViewHolder(resultBinding) {
        override fun bind(ingredient: AbstractIngredient) {
            resultBinding.result = ingredient as IngredientResult
            binding.root.setOnClickListener {
                fragment.addGrocery(ingredient.id)
                notifyDataSetChanged()
            }
        }
    }
}