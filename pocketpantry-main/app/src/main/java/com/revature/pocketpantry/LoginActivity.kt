package com.revature.pocketpantry

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.revature.pocketpantry.databinding.ActivityLoginBinding
import com.revature.pocketpantry.network.UserDatabaseController
import kotlinx.coroutines.*
import java.util.regex.Matcher


class LoginActivity : AppCompatActivity() {

    lateinit var binding:ActivityLoginBinding
    lateinit var username:TextView
    lateinit var password:TextView
    lateinit var infoBtn:ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivLogo.foregroundTintList
        binding.root
        Toast.makeText(this, "This program is provided as-is. No responsibility is accepted for any issue or problem deriving from use of this program. " +
        "This contains known security holes that will not be patched. Use at own risk, and never provide critical or personal information", Toast.LENGTH_LONG).show()
        Log.d("theme", R.attr.isLightTheme.toString())
        var value = TypedValue()
        var b = theme.resolveAttribute(R.attr.isLightTheme, value , true)
        Log.d("theme", "isLight? $b")

        this.title = "Login To Pocket Pantry"
        val loginBtn:Button=findViewById(R.id.login_btn)
        val registerBtn:Button = findViewById(R.id.register_btn)

        infoBtn = findViewById(R.id.imageButton)
        infoBtn.setOnClickListener{
            Toast.makeText(this, "This program is provided as-is. No responsibility is accepted for any issue or problem deriving from use of this program. " +
                    "This contains known security holes that will not be patched. Use at own risk, and never provide critical or personal information", Toast.LENGTH_LONG).show()
        }
        username = binding.usernameTextView
        password = binding.passwordTextView

        //Launches a coroutine to login
        //This is necessary because accessing the firebase is asynchronus, so we have to wait
        //for it to tell us if we logged in or not
        loginBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.Default){
              //  Log.i("MainActivity", "Call1")
                UserDatabaseController.login(binding.usernameTextView.text.toString(), binding.passwordTextView.text.toString(), this@LoginActivity)
           }
        }
        //This launches a coroutine to access firebase and register a new user
        registerBtn.setOnClickListener {
            GlobalScope.async{
                Log.i("MainActivity", "Register Attempt")
                UserDatabaseController.register(binding.usernameTextView.text.toString(), binding.passwordTextView.text.toString(), this@LoginActivity)
            }
        }

    }

    //This ensures that when a user comes back to the screen, the user and password fields are empty
    override fun onResume() {
        binding.usernameTextView.text.clear()
        binding.passwordTextView.text.clear()
        super.onResume()
    }
}