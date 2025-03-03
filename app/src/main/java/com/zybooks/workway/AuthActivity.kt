package com.zybooks.workway

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AuthActivity : AppCompatActivity() {
    private lateinit var googleSignInButton: Button

    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Authentication.doGoogleSignIn(
                context = this,
                scope = coroutineScope,
                launcher = null,
                login = { }
            )
        }

        googleSignInButton = findViewById(R.id.authButton)
        googleSignInButton.setOnClickListener {
            Authentication.doGoogleSignIn(
                context = this,
                scope = coroutineScope,
                launcher = googleSignInLauncher,
                login = { onLoginSuccess() }
            )
        }

        if (intent.getBooleanExtra("LOGOUT", false)) onLogout()
    }

    private fun onLoginSuccess() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val text = "Logged in as " + user?.email
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        googleSignInButton.text = text
        googleSignInButton.isEnabled = false
        startActivity(Intent(this, HomeScreen::class.java))
    }

    private fun onLogout() {
        return
    }
}