package com.revature.pocketpantry.profile

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.*
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.revature.pocketpantry.R
import com.revature.pocketpantry.databinding.ActivityNavigationBinding
import com.revature.pocketpantry.databinding.FragmentProfileBinding
import com.revature.pocketpantry.model.CurrentUser
import com.revature.pocketpantry.network.UserDatabaseController


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    //This code is done here, as the fragment does not actually store any information and I had difficulty setting
    //up live data to work with a chipgroup
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var chips:ChipGroup = view.findViewById(R.id.profile_chipGroup)
        //For each of the chips in the group,
        //check if they are supposed to be checked (in CurrentUser), if so, check them.
        //Then add a click listener, so that if someone checks them, it updates the Current User

        for(i in 0..chips.childCount) {
            var x = chips.getChildAt(i)
            if (x != null) {
                if (CurrentUser.preferences.contains((x as Chip).text.toString())|| CurrentUser.diet == (x as Chip).text.toString()) {
                    Log.i("CHIPGROUP", "${x.text} Should be clicked")
                    chips.check(x.id)
                }
                x.setOnClickListener {
                    //There's some special code for Vegetarian, as it is a diet, not a preference
                    //Given enough time, I will have multiple diets, and clicking one will unclick the others
                    if (x.text.toString() == "Vegetarian Only") {
                        if(CurrentUser.diet == x.text.toString())
                            CurrentUser.diet = null
                        else
                            CurrentUser.diet = x.text.toString()
                    } else {
                        if (!CurrentUser.preferences.contains(x.text.toString()))
                            CurrentUser.preferences.add(x.text.toString())
                        else
                            CurrentUser.preferences.remove(x.text.toString())
                    }
                }
            }
        }
    }

    private lateinit var binding: FragmentProfileBinding
    private lateinit var searchBtn:Button
    private lateinit var logOutBtn:Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val textView: TextView = root.findViewById(R.id.profile_title_text)
        profileViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        binding = FragmentProfileBinding.inflate(inflater)
        searchBtn = binding.profileSearchBtn
        logOutBtn = binding.profileLogOutBtn
        //this sets the button that sends the user to the search screen
        searchBtn.setOnClickListener {
            Log.i("Profile Button", "Clicked")
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_home_search)

        }
        logOutBtn.setOnClickListener {
            activity?.finish()
            requireActivity().finish()
        }


        return  binding.root
    }


    //This means whenever someone navigates away from the page, it will update the database
    //I didn't want to update the database every time someone clicked, so it waits until they
    //are finished with this page
    override fun onPause() {
        UserDatabaseController.update()
        super.onPause()
    }

}