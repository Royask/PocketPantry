package com.revature.pocketpantry.pantry

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.revature.pocketpantry.databinding.PantryFragmentBinding
import com.revature.pocketpantry.model.*
import com.revature.pocketpantry.network.UserDatabaseController
import com.revature.pocketpantry.util.IngredientSearchAdapter
import com.revature.pocketpantry.util.SearchableFragment
import com.revature.pocketpantry.util.SearchableViewModel
import com.revature.pocketpantry.util.SwipeHelper
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class Pantry: SearchableFragment() {

    override val viewModel: PantryViewModel by viewModels()
    private lateinit var binding: PantryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = PantryFragmentBinding.inflate(inflater).run {

        binding = this

        lifecycleOwner = this@Pantry
        viewModel = this@Pantry.viewModel

        rvPantry.adapter  = PantryAdapter()
        rvStaples.adapter = PantryAdapter()
        rvPantrySearch.adapter = IngredientSearchAdapter(this@Pantry)

        btnAddToPantry.setOnClickListener { startSearch() }
        btnStaples.setOnClickListener { toggleStaples() }

        SwipeHelper(rvPantry).attach()
        SwipeHelper(rvStaples).attach()

        root
    }
    override fun onPause() {
        UserDatabaseController.update()
        super.onPause()
    }

    override fun addGrocery(id: Int) {
        viewModel.addGrocery(id)
        binding.llPantrySearch.visibility = View.GONE
    }

    private fun toggleStaples() {
        binding.btnStaples.updateLayoutParams<ConstraintLayout.LayoutParams> {
            verticalBias = 1f - verticalBias
        }
    }

    private fun startSearch() {
        binding.llPantrySearch.visibility = View.VISIBLE
        binding.svPantrySearch.isIconified = false
        binding.llPantrySearch.setOnClickListener { it.visibility = View.GONE }
        binding.svPantrySearch.setOnQueryTextListener(QueryTextListener(binding.rvPantrySearch))
    }

}



// ****** dummy data ****** //

val dummyPantry = mutableListOf(
    Ingredient(name="water-packed tuna", id=15121, original="water-packed tuna", originalName="water-packed tuna", possibleUnits=listOf("can", "piece", "g", "tin", "ounce", "oz", "cup", "serving", ), consistency="solid", aisle="Canned and Jarred", image="canned-tuna.png", categoryPath=listOf("canned tuna", "tuna", "fish", "seafood", ), meta=listOf()),
    Ingredient(name="pepper sauce", id=6168, original="pepper sauce", originalName="pepper sauce", possibleUnits=listOf("drop", "g", "jar", "oz", "dash", "dashe", "teaspoon", "cup", "serving", "tablespoon", ), consistency="liquid", aisle="Condiments", image="hot-sauce-or-tabasco.png", categoryPath=listOf("condiment", ), meta=listOf()),
    Ingredient(name="green peas", id=11304, original="green peas", originalName="green peas", possibleUnits=listOf("can", "package", "g", "tin", "bag", "box", "oz", "teaspoon", "cup", "serving", "tablespoon", ), consistency="solid", aisle="Produce", image="peas.jpg", categoryPath=listOf("peas", "vegetable", ), meta=listOf()),
    Ingredient(name="noodles", id=20420, original="noodles", originalName="noodles", possibleUnits=listOf("square", "package", "piece", "g", "bag", "ounce", "box", "sheet", "nest", "oz", "cup", "serving", ), consistency="solid", aisle="Pasta and Rice", image="fusilli.jpg", categoryPath=listOf("main dish", ), meta=listOf()),
    Ingredient(name="parsley leaves", id=11297, original="parsley leaves", originalName="parsley leaves", possibleUnits=listOf("handful", "piece", "leave", "g", "sprig", "bunch", "oz", "teaspoon", "cup chopped", "cup", "serving", "tablespoon", ), consistency="solid", aisle="Produce;Spices and Seasonings", image="parsley.jpg", categoryPath=listOf("herbs", ), meta=listOf()),
    Ingredient(name="bacon strips", id=10123, original="bacon strips", originalName="bacon strips", possibleUnits=listOf("slice raw", "package", "strip", "rasher", "piece", "slice", "g", "oz", "serving", ), consistency="solid", aisle="Meat", image="raw-bacon.png", categoryPath=listOf("processed pork", "processed meat", "meat", ), meta=listOf()),
    Ingredient(name="parmesan cheese", id=1033, original="parmesan cheese", originalName="parmesan cheese", possibleUnits=listOf("cubic inch", "package", "g", "ounce", "oz", "teaspoon", "cup", "serving", "tablespoon", ), consistency="solid", aisle="Cheese", image="parmesan.jpg", categoryPath=listOf("hard cheese", "cheese", ), meta=listOf()),
    Ingredient(name="onions", id=11282, original="onions", originalName="onions", possibleUnits=listOf("small", "large", "tbsp chopped", "ring", "g", "medium", "oz", "teaspoon", "serving", "tablespoon", "piece", "slice", "cup", ), consistency="solid", aisle="Produce", image="brown-onion.png", categoryPath=listOf(), meta=listOf()),
    Ingredient(name="potatoes", id=11352, original="potatoes", originalName="potatoes", possibleUnits=listOf("small", "large", "piece", "Potato medium", "g", "medium", "Potato large", "oz", "cup", "Potato small", ), consistency="solid", aisle="Produce", image="potatoes-yukon-gold.png", categoryPath=listOf(), meta=listOf()),
    Ingredient(name="bread roll dough", id=99063, original="bread roll dough", originalName="bread roll dough", possibleUnits=listOf("ball", "piece", "g", "recipe", "loaf", "oz", ), consistency="solid", aisle="Frozen", image="pizza-dough.jpg", categoryPath=listOf("dough", ), meta=listOf()),
    Ingredient(name="garlic", id=11215, original="garlic", originalName="garlic", possibleUnits=listOf("clove", "head", "piece", "g", "oz", "teaspoon", "cup", "serving", "tablespoon", ), consistency="solid", aisle="Produce", image="garlic.png", categoryPath=listOf(), meta=listOf()),
    Ingredient(name="boneless chicken breast", id=5062, original="boneless chicken breast", originalName="boneless chicken breast", possibleUnits=listOf("unit", "piece", "g", "oz", "breast", "cup", "serving", ), consistency="solid", aisle="Meat", image="chicken-breasts.png", categoryPath=listOf("chicken breast", "chicken", "poultry", "meat", ), meta=listOf())
)

val dummyStaples = mutableListOf(
    Ingredient(name="white salt", id=2047, original="white salt", originalName="white salt", possibleUnits=listOf("pinch", "g", "oz", "dash", "teaspoon", "cup", "serving", "tablespoon", ), consistency="solid", aisle="Spices and Seasonings", image="salt.jpg", categoryPath=listOf(), meta=listOf()),
    Ingredient(name="ground pepper", id=1002030, original="ground pepper", originalName="ground pepper", possibleUnits=listOf("pinch", "g", "oz", "dash", "teaspoon", "serving", "tablespoon", ), consistency="solid", aisle="Spices and Seasonings", image="pepper.jpg", categoryPath=listOf("black pepper", "pepper", "spices", ), meta=listOf()),
    Ingredient(name="garlic powder", id=1022020, original="garlic powder", originalName="garlic powder", possibleUnits=listOf("pinch", "g", "oz", "dash", "teaspoon", "serving", "tablespoon", ), consistency="solid", aisle="Spices and Seasonings", image="garlic-powder.png", categoryPath=listOf("dehydrated garlic", "spices", ), meta=listOf()),
    Ingredient(name="ground onion", id=2026, original="ground onion", originalName="ground onion", possibleUnits=listOf("g", "oz", "teaspoon", "cup", "tablespoon", ), consistency="solid", aisle="Spices and Seasonings", image="onion-powder.jpg", categoryPath=listOf("dried onion", "onion", ), meta=listOf()),
    Ingredient(name="pepper sauce", id=6168, original="pepper sauce", originalName="pepper sauce", possibleUnits=listOf("drop", "g", "jar", "oz", "dash", "dashe", "teaspoon", "cup", "serving", "tablespoon", ), consistency="liquid", aisle="Condiments", image="hot-sauce-or-tabasco.png", categoryPath=listOf("condiment", ), meta=listOf()),
    Ingredient(name="eggs", id=1123, original="eggs", originalName="eggs", possibleUnits=listOf("small", "jumbo", "large", "piece", "g", "medium", "oz", "extra large", "dozen", "cup", "serving", ), consistency="solid", aisle="Milk, Eggs, Other Dairy", image="egg.png", categoryPath=listOf(), meta=listOf()),
    Ingredient(name="pure olive oil", id=4053, original="pure olive oil", originalName="pure olive oil", possibleUnits=listOf("spoonful", "g", "oz", "teaspoon", "cup", "serving", "tablespoon", ), consistency="liquid", aisle="Oil, Vinegar, Salad Dressing", image="olive-oil.jpg", categoryPath=listOf("cooking oil", "cooking fat", ), meta=listOf()),
    Ingredient(name="fat-free milk", id=1085, original="fat-free milk", originalName="fat-free milk", possibleUnits=listOf("quart", "g", "oz", "teaspoon", "fluid ounce", "cup", "serving", "tablespoon", ), consistency="liquid", aisle="Milk, Eggs, Other Dairy", image="milk.jpg", categoryPath=listOf("milk", "drink", ), meta=listOf()),
    Ingredient(name="spring onion", id=11291, original="spring onion", originalName="spring onion", possibleUnits=listOf("small", "large", "tbsp chopped", "g", "rib", "medium", "oz", "teaspoon", "serving", "tablespoon", "piece", "stalk", "sprig", "bunch", "cup", ), consistency="solid", aisle="Produce", image="spring-onions.jpg", categoryPath=listOf("onion", ), meta=listOf()),
    Ingredient(name="wheat flour", id=20081, original="wheat flour", originalName="wheat flour", possibleUnits=listOf("g", "oz", "teaspoon", "cup", "serving", "tablespoon", ), consistency="solid", aisle="Baking", image="flour.png", categoryPath=listOf("flour product", ), meta=listOf()),
    Ingredient(name="dairy milk", id=1077, original="dairy milk", originalName="dairy milk", possibleUnits=listOf("quart", "g", "oz", "teaspoon", "fluid ounce", "cup", "serving", "tablespoon", ), consistency="liquid", aisle="Milk, Eggs, Other Dairy", image="milk.png", categoryPath=listOf("drink", ), meta=listOf()),
    Ingredient(name="Better for Bread flour", id=10120129, original="Better for Bread flour", originalName="Better for Bread flour", possibleUnits=listOf("spoonful", "g", "cup unsifted", "oz", "teaspoon", "cup", "serving", "tablespoon", ), consistency="solid", aisle="Baking", image="flour.png", categoryPath=listOf("flour product", ), meta=listOf())
)
