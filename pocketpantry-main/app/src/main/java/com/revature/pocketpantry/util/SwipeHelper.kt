package com.revature.pocketpantry.util

import androidx.fragment.app.*
import androidx.recyclerview.widget.*
import com.revature.pocketpantry.R
import com.revature.pocketpantry.pantry.*
import com.revature.pocketpantry.shoppingList.*

open class SwipeHelper(
    var recyclerView: RecyclerView,
    var swipeDirs: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
): ItemTouchHelper.SimpleCallback(ItemTouchHelper.ACTION_STATE_IDLE, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean { return false }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (val fragment = recyclerView.findFragment<Fragment>()) {
            is Pantry -> {
                (viewHolder as PantryAdapter.GroceryViewHolder).pantryBinding.grocery?.let {
                    when (recyclerView.id) {
                        R.id.rv_pantry -> fragment.viewModel.removeGrocery(it)
                        R.id.rv_staples -> fragment.viewModel.removeStaple(it)
                        else -> {}
                    }
                }
            }
            is ShoppingList -> {
                (viewHolder as GroceryAdapter.GroceryViewHolder).groceryBinding.grocery?.let {
                    //fragment.shoppingListViewModel.removeGrocery(it)
                    fragment.viewModel.removeGrocery(it)
                }
            }
        }
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun attach() { ItemTouchHelper(this).attachToRecyclerView(recyclerView) }
}