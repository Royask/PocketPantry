package com.revature.pocketpantry

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.revature.pocketpantry.model.CurrentUser
import com.revature.pocketpantry.network.UserDatabaseController


class Navigation : AppCompatActivity() {

    private lateinit var navView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
       // navView = findViewById(R.id.mobile_navigation)

        CurrentUser.loadRecipes()
        val navView: BottomNavigationView = findViewById(R.id.nav_view_drawer)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home_search, R.id.navigation_profile, R.id.navigation_Saved_Recipes,
                R.id.navigation_pantry, R.id.navigation_shoppingList
            )
        )
        NavigationUI.setupActionBarWithNavController(this,navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)

        //this makes the navigation bar disappear on certain fragments
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.selectedRecipeFragment){
                navView.visibility = View.GONE
            }
            else{
                navView.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController,AppBarConfiguration(
            setOf(

                R.id.navigation_home_search, R.id.navigation_profile, R.id.navigation_Saved_Recipes,
                R.id.navigation_pantry, R.id.navigation_shoppingList
            )
        ))
    }
}