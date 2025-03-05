package com.zybooks.workway

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.zybooks.workway.database.DBHelper
import com.zybooks.workway.repository.CalendarRepository
import com.zybooks.workway.repository.ChatRepository
import com.zybooks.workway.repository.CompanyRepository
import com.zybooks.workway.repository.MeetingRepository
import com.zybooks.workway.repository.UserRepository

class MainActivity : AppCompatActivity() {
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        authManager = AuthManager(this)

        testDatabaseFunctions()
    }

    override fun onResume() {
        super.onResume()
        authManager.run()
    }

    private fun testDatabaseFunctions() {
        val dbHelper = DBHelper(this)
        val companyRepo = CompanyRepository(this)
        val userRepo = UserRepository(this)
        val meetingRepo = MeetingRepository(this)
        val calendarRepo = CalendarRepository(this)
        val chatRepo = ChatRepository(this)

        // Insert dummy data
        dbHelper.insertDummyData()
        dbHelper.insertDummyCompanies(this)

        // Fetch and print all users
        val users = userRepo.getAllUsers()
        for (user in users) {
            Log.d("DB_TEST", "UserID: ${user.userID}, Name: ${user.name}, Email: ${user.email}")
        }
        val columns = dbHelper.getTableColumns("messages")
        Log.d("Column_names", "Columns in messages table: $columns")

        val meetings = meetingRepo.getMeetings()
        for (meeting in meetings) {
            Log.d("DB_TEST", "MeetingID: ${meeting.meetingID}, Title: ${meeting.title}, Description: ${meeting.description}")
        }

        val calendars = calendarRepo.getCalendarEvents()
        for (calendar in calendars) {
            Log.d("DB_TEST", "CalendarID: ${calendar.eventID}, Title: ${calendar.title}, Description: ${calendar.description}")
        }

        val status = companyRepo.authenticateUser("alice@onu.edu", "password123", "ONU123")
        Log.d("DB_TEST_COMP", "Authentication Status: $status")

        val messages = chatRepo.getMessages(chatId = 1)

        for (msg in messages) {
            Log.d("DB_TEST_MESSAGES","${msg.senderID}: ${msg.messageText} [${msg.timestamp}]")
        }

    }

    fun onSignInClick(view: View){
        val intent = Intent(this, HomeScreen::class.java)
        startActivity(intent)
        this.finish()
        Log.i("Login", "Destroyed login page activity")
    }
}