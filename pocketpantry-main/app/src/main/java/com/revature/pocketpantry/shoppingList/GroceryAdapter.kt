package com.revature.pocketpantry.shoppingList

import android.view.*
import androidx.fragment.app.findFragment
import com.revature.pocketpantry.databinding.ShoppingListItemBinding
import com.revature.pocketpantry.model.*
import com.revature.pocketpantry.util.IngredientAdapter

/**
 * This is the adaptor class that will contain the Ingredients for the ShoppingList RecyclerView
 * @author Alec Ramirez
 */
class GroceryAdapter: IngredientAdapter<Ingredient, GroceryAdapter.GroceryViewHolder>() {

    /**
     * Returns an instance of GroceryViewHolder with an inflated ShoppingListItemBinding using
     * the parent ViewGroup context
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder =
        GroceryViewHolder(ShoppingListItemBinding.inflate(LayoutInflater.from(parent.context)))

    /**
     * This calls that parent onBindViewHolder() after upcasting the GroceryViewHolder to a more
     * generic IngredientViewHolder.
     */
    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        super.onBindViewHolder(holder as IngredientViewHolder, position)
    }

    /**
     * This is the ViewHolder class that will hold the ViewDataBinding object for each list item
     * in the ShoppingList RecyclerView.  It also has the bind() function which sets the
     * Ingredient for each list item.
     */
    class GroceryViewHolder(var groceryBinding: ShoppingListItemBinding): IngredientViewHolder(groceryBinding) {
        override fun bind(ingredient: AbstractIngredient) {
            // Assert to the compiler that ingredient is an instance of Ingredient and not a
            // a different child class of AbstractIngredient
            if(ingredient !is Ingredient) return

            /**
             * Here we set the OnClickListener for each list item.  This simply enables the user
             * to click anywhere within the list item View to check/uncheck the CheckBox rather
             * than having to click directly on the checkbox.
             */
            itemView.setOnClickListener {
                groceryBinding.cbShop.run { isChecked = !isChecked }

                groceryBinding.root.findFragment<ShoppingList>().viewModel.let {
                    bindingAdapter?.notifyDataSetChanged()
                }
            }

            /** Set the variable grocery:Ingredient of the list item View */
            groceryBinding.grocery = ingredient
        }
    }
}