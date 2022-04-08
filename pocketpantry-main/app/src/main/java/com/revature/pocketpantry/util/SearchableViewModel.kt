package com.revature.pocketpantry.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revature.pocketpantry.model.IngredientResult
import com.revature.pocketpantry.network.SpoonApi
import kotlinx.coroutines.launch

open class SearchableViewModel: ViewModel() {

    val retro = SpoonApi.retrofitService

    protected val _results = MutableLiveData<MutableList<IngredientResult>>()
    val results: LiveData<MutableList<IngredientResult>>
        get() = _results

    open fun search(query: String) {
        viewModelScope.launch {
            _results.value = retro.searchIngredients(query).results
        }
    }

    open fun autoCompleteSearch(query: String) {
        viewModelScope.launch {
            _results.value = retro.autocompleteIngredients(query)
        }
    }

}