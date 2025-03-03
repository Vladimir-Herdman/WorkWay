package com.zybooks.workway

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val activity = if (FirebaseAuth.getInstance().currentUser != null) HomeScreen::class.java else AuthActivity::class.java;
        startActivity(Intent(this, activity))
        finish()
    }

    fun onSignInClick(view: View){
        val intent = Intent(this, HomeScreen::class.java)
        startActivity(intent)
        this.finish()
        Log.i("Login", "Destroyed login page activity")
    }
}