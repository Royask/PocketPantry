@file:Suppress("UNCHECKED_CAST")

package com.revature.pocketpantry.network

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.FirebaseError
import com.google.firebase.database.*
import com.revature.pocketpantry.model.CurrentUser
import org.json.JSONObject
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import com.google.firebase.ktx.Firebase
import com.revature.pocketpantry.Navigation
import com.revature.pocketpantry.model.Ingredient
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

object UserDatabaseController {
    var currentUser: CurrentUser = CurrentUser
    private val database = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = database.getReference("Users")


    public suspend fun login(username: String, password: String, context:Context) {

        if(username == "" || password == "")
        {
          //  Toast.makeText(context, "Username/Password Invalid", Toast.LENGTH_LONG).show()
            return
        }

        //BE AWARE THAT THIS FUNCTION REQUIRES CONTEXT!
        //I had issues properly getting coroutines to wait for the firebase data
        //My answer was to start the activity from this coroutine
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value: Map<String, Objects> = dataSnapshot.value as Map<String, Objects>
                try {
                    //Checks if user list exists
                    val jsonObject: JSONObject = JSONObject(value)
                    try {
                        //checks if user exists
                        val jsonUser: JSONObject = jsonObject.getJSONObject(username)
                        try {
                            //if they exist and their password matches, load their information into the local user
                            if (jsonUser.get("name") == username && jsonUser.get("password") == password) {
                                Log.w("Database Access Success", "User: $username Logged in!")
                                currentUser.clear()
                                currentUser.name = username
                                currentUser.password = password
                                //This is bad code. I am aware, but unable to spare the time to properly code the correct way
                                //This is functional, but costly
                                try {
                                    currentUser.diet = jsonUser.get("diet") as String
                                } catch (e: Exception) {
                                    currentUser.diet = ""
                                }
                                try {
                                    currentUser.uuid = jsonUser.get("uuid") as String
                                    Log.i("Login", "Successfully Extracted User ID")
                                } catch (e: Exception) {
                                    Log.i("Login", "Failed to extract User ID")
                                    currentUser.uuid = "-1"
                                }
                                try {
                                    currentUser.savedRecipeIDs.clear()
                                    var arr = jsonUser.getJSONArray("savedRecipeIDs")
                                    for(x:Int in 0 until arr.length()) {
                                        currentUser.savedRecipeIDs.add(arr.getInt(x))
                                    }
                                    Log.i("Login", "Successfully extracted Saved Recipe List")
                                } catch (e: Exception) {
                                    Log.i("Login", "Failed to extract Saved Recipe List")
                                   // currentUser.clear()
                                }
                                try {
                                    currentUser.appliances.clear()
                                    var arr = jsonUser.getJSONArray("appliances")
                                    for(x:Int in 0 until arr.length()) {
                                        currentUser.appliances.add(arr.getInt(x))
                                    }
                                    Log.i("Login", "Successfully extracted Appliance List")
                                } catch (e: Exception) {
                                    Log.i("Login", "Failed to extract Appliance List")
                                }
                                try {
                                    currentUser.supplies =
                                        jsonUser.get("supplies") as MutableList<Triple<Int, Int, String>>
                                } catch (e: Exception) {
                                    currentUser.supplies =
                                        mutableListOf<Triple<Int, Int, String>>()
                                }
                                try {
                                    //this section currently nonfunctional due to difficulties storing triples in Firebase
                                    currentUser.staples.clear()
                                   /*
                                    currentUser.staples.clear()
                                    var arr = jsonUser.getJSONArray("staples")
                                    for(x:Int in 0 until arr.length()) {
                                        var temp = arr.getJSONArray("staples")
                                        currentUser.shoppingList.add(arr.getInt(x))
                                    }
                                    Log.i("Login", "Successfully extracted List of Staples")*/
                                } catch (e: Exception) {
                                    Log.i("Login", "Failed to extract List of Staples")
                                }
                                try {

                                    currentUser.shoppingList.clear()
                                    var arr = jsonUser.getJSONArray("shoppingList")
                                    for(x:Int in 0 until arr.length()) {
                                        currentUser.shoppingList.add(arr.getInt(x))
                                    }
                                    Log.i("Login", "Successfully extracted Shopping List")
                                } catch (e: Exception) {
                                    Log.i("Login", "Failed to extract Shopping List")
                                }
                                try {
                                    currentUser.preferences.clear()
                                    var arr = jsonUser.getJSONArray("preferences")
                                    for(x:Int in 0 until arr.length()) {
                                        currentUser.preferences.add(arr.getString(x))
                                    }
                                    Log.i("Login", "Successfully extracted Preferences")
                                } catch (e: Exception) {
                                    Log.i("Login", "Failed to extract Preferences")
                                }
                                try {

                                    currentUser.groceryListIDs.clear()
                                    var arr = jsonUser.getJSONArray("groceryListIDs")
                                    for(x:Int in 0 until arr.length()) {
                                        currentUser.groceryListIDs.add(arr.getInt(x))
                                    }
                                    Log.i("Login", "Successfully extracted Grocery IDs")

                                } catch (e: Exception) {
                                    Log.i("Login", "Failed to extract Grocery IDS")
                                }
                                try {

                                    currentUser.groceryList.clear()
                                    var arr = jsonUser.getJSONArray("groceryList")
                                    for(x:Int in 0 until arr.length()) {
                                        currentUser.groceryList.add(arr.getJSONObject(x)as Ingredient)
                                    }
                                    Log.i("Login", "Successfully extracted Grocery IDs")
                                } catch (e: Exception) {
                                    Log.i("Login", "Failed to extract Grocery IDS")
                                }

                                Log.i(
                                    "Database Access Success: ",
                                    "INNER\nName: ${currentUser.name}\nPassword: ${currentUser.password}" +
                                            "\nUser ID: ${currentUser.uuid}\nPreferences: ${currentUser.preferences}\nSupplies: ${currentUser.supplies}\nShoppingList${currentUser.shoppingList}\n"
                                )

                                //This launches another coroutine to populate the shopping list with ingredients based on the recipes we retrieved from the user's profile on firebase
                                //It has to be launched here, tso that it is after that list of recipes is populated
                                GlobalScope.launch(Dispatchers.Default){
                                    currentUser.groceryList.clear()
                                    currentUser.groceryListIDs.forEach { currentUser.groceryList.add(
                                        SpoonApi.retrofitService.getIngredient(it)) }
                                }
                                //Here, we have successfully logged in and are starting the main activity
                                startActivity(context,Intent(context, Navigation::class.java), null)
                            } else {
                                Log.w(
                                    "Database Access Success",
                                    "User: $username Failed to log in, Username/password mismatch"
                                )
                                Toast.makeText(context, "Username/Password Mismatch", Toast.LENGTH_LONG).show()

                            }
                        } catch (e: Exception) {
                            Log.w(
                                "Database Access Success",
                                "User: $username Failed to log in"
                            )

                        }

                    } catch (e: Exception) {

                        Toast.makeText(context, "Username Not Recognized", Toast.LENGTH_LONG).show()
                        Log.w(
                            "Database Access Failure",
                            "$username Not Registered"
                        )
                        e.printStackTrace()
                    }

                } catch (e: Exception) {
                    Log.d("Login:", "$username is not registered")

                }

            }


            override fun onCancelled(error: DatabaseError) {
                Log.w("Database Access Failure", "Failed to read value.", error.toException())
            }
        })


    }


    //This function is called on every fragment inside the opPause function
    //it updates the database with any changes that have been made
    public fun update() {
        try
            {
                //This catches errors where somehow the user's name is not yet set
                if(currentUser.name.isNotEmpty()) {
                    try {
                   //     myRef.setValue(currentUser.name)
                        myRef.child(currentUser.name).setValue(currentUser)
                    }catch (e:Exception)
                    {
                        e.printStackTrace()
                     //   myRef.child(currentUser.name).setValue(currentUser)
                    }



                }
            }
            catch (e:Exception)
            {
                Log.i("Firebase Update", "Operation Failed")
                e.printStackTrace()
            }

    }

    //BE AWARE THAT THIS FUNCTION REQUIRES CONTEXT!
    //I had issues properly getting coroutines to wait for the firebase data
    //My answer was to start the activity from this coroutine
    public fun register(username: String, password: String, context:Context)
    {

        if(username.isEmpty() || password.isEmpty())
        {
         //   Toast.makeText(context, "Username/Password Invalid", Toast.LENGTH_LONG).show()
            return
        }


        //This contains all the code for going to the firebase, checking if we can register the user, and if so, registering them
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value: Map<String, Objects> = dataSnapshot.value as Map<String, Objects>
                try {
                    //If this throws an exception, it means the database is empty. If so, auto-register the user (and so create the database)
                    val jsonObject: JSONObject = JSONObject(value)
                    try {
                        //if this does not throw an exception, we successfully accessed the database, meaning the username is already registered
                        val jsonUser: JSONObject = jsonObject.getJSONObject(username)
                        Log.i("Register", "Username already exists")
                        Toast.makeText(context, "Username already Exists!", Toast.LENGTH_LONG).show()
                    }
                    catch(e:Exception)
                    {
                        //This is called if the user does not exist in the list of users: register and Log them In!
                        currentUser.clear()
                        currentUser.name = username
                        currentUser.password = password
                        myRef.child(username).setValue(currentUser)
                        startActivity(context,Intent(context, Navigation::class.java), null)
                    }

                }catch (e:Exception)
                {
                    //This is called if the Json call fails (Userbase not found, first user? auto register/login)
                    currentUser.clear()
                    currentUser.name = username
                    currentUser.password = password
                    myRef.child(username).setValue(currentUser)
                    startActivity(context,Intent(context, Navigation::class.java), null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Register", "Operation was cancelled")
            }
        })

    }



    //This is the User class, it is not currently used
    //It was intended to hold an instance of a User, but we decided to only hold one user at a time
    class User() {
       var savedRecipes = mutableListOf<Int>()
       var shoppingList = mutableListOf<Int>()
       var name: String = "Mrs Tester"
       var password: String = ""
       var uuid: String = "000000001"
       var appliances: ArrayList<Int>? = ArrayList<Int>()
       var supplies: MutableList<Triple<Int, Int, String>> =
           mutableListOf<Triple<Int, Int, String>>()
       var staples: MutableList<Triple<Int, Int, String>> = ArrayList<Triple<Int, Int, String>>()
       var preferences: MutableList<String> = mutableListOf()
       fun copy():User
       {
           savedRecipes = CurrentUser.savedRecipeIDs
           shoppingList = CurrentUser.shoppingList
           name = CurrentUser.name
           password = CurrentUser.password
           uuid= CurrentUser.uuid
           appliances= CurrentUser.appliances
           supplies=CurrentUser.supplies
           staples= CurrentUser.staples
           preferences = CurrentUser.preferences
           return this
       }
    }
}

