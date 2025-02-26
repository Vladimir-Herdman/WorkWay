package com.zybooks.workway

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeScreen : AppCompatActivity() {
    private lateinit var optionsMenu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Upper toolbar
        val toolbar: MaterialToolbar = findViewById(R.id.myToolbar)
        toolbar.elevation = 8f
        toolbar.overflowIcon?.setTint(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)

        // Bottom menu and fragment selections
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        val trainingFragment = Training()
        val meetingsFragment = Meetings()
        val messagesFragment = Messages()
        val homeFragment = Home()

        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_training -> setCurrentFragment(trainingFragment)
                R.id.action_meetings -> setCurrentFragment(meetingsFragment)
                R.id.action_messages -> setCurrentFragment(messagesFragment)
                R.id.action_home -> setCurrentFragment(homeFragment)
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        optionsMenu = menu!!
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Determine which menu option was chosen
        return when (item.itemId) {
            R.id.action_logout -> {
                Log.i("AppBar", "Logout Button Pressed")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        Log.i("Fragment", "Replacing with ${fragment.javaClass.simpleName}")
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commitAllowingStateLoss()
        }
    }
}