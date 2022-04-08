package com.revature.pocketpantry.util

import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

abstract class SearchableFragment: Fragment() {

    abstract val viewModel: SearchableViewModel

    abstract fun addGrocery(id: Int)

    inner class QueryTextListener(val resultRecycler: RecyclerView): SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let { viewModel.search(it) }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            resultRecycler.visibility = if (newText.isNullOrBlank()) View.GONE else View.VISIBLE
            newText?.let { viewModel.autoCompleteSearch(it) }
            return false
        }
    }
}