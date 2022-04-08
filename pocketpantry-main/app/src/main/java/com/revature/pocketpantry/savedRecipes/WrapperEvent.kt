package com.revature.pocketpantry.savedRecipes

import com.revature.pocketpantry.model.Recipe

/**
 * This is the wrapper event that contains if it has been handled or not.
 * @author Jacob Ginn
 */
class WrapperEvent<out T>(private val content : T) {

    var hasBeenViewed = false
        private set

    fun getRecipeIfNotHandled(): T?{
        return if (hasBeenViewed){
            null
        }else{
            hasBeenViewed = true
            content
        }
    }

    fun peekContent() : T = content
}