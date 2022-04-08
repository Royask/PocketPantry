package com.revature.pocketpantry.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.revature.pocketpantry.model.CurrentUser
import com.revature.pocketpantry.model.Recipe

class ProfileViewModel: ViewModel() {

    //I cannot get the viewModel to work with the chipgroup, so the only thing I had to implement
    // was the title, which is the username of the client
    private val _text = MutableLiveData<String>().apply {
        value = if(CurrentUser.name.isNotEmpty())
            CurrentUser.name
        else
            "Your Culinary Preferences"
    }
    val text: LiveData<String> = _text


}