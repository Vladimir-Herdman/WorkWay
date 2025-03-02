package com.zybooks.workway.repository

import android.content.ContentValues
import android.content.Context
import com.zybooks.workway.database.DBHelper
import com.zybooks.workway.model.User

class UserRepository(context: Context) {
    private val dbHelper = DBHelper(context)

    // Insert User
    fun insertUser(user: User): Long {
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_USER_NAME, user.name)
            put(DBHelper.COLUMN_USER_EMAIL, user.email)
            put(DBHelper.COLUMN_USER_PASSWORD, user.password)
        }
        return dbHelper.insertData(DBHelper.TABLE_USERS, values)
    }

    // Read Users
    fun getAllUsers(): List<User> {
        val userList = mutableListOf<User>()
        val cursor = dbHelper.getData(DBHelper.TABLE_USERS, arrayOf(DBHelper.COLUMN_USER_ID, DBHelper.COLUMN_USER_NAME, DBHelper.COLUMN_USER_EMAIL, DBHelper.COLUMN_USER_PASSWORD))

        while (cursor.moveToNext()) {
            val user = User().apply {
                userID = cursor.getString(0)
                name = cursor.getString(1)
                email = cursor.getString(2)
                password = cursor.getString(3)
            }
            userList.add(user)
        }
        cursor.close()
        return userList
    }

    // Update User
    fun updateUser(userID: String, name: String, email: String, password: String): Int {
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_USER_NAME, name)
            put(DBHelper.COLUMN_USER_EMAIL, email)
            put(DBHelper.COLUMN_USER_PASSWORD, password)
        }
        return dbHelper.updateData(DBHelper.TABLE_USERS, values, "${DBHelper.COLUMN_USER_ID}=?", arrayOf(userID))
    }

    // Delete User
    fun deleteUser(userID: String): Int {
        return dbHelper.deleteData(DBHelper.TABLE_USERS, "${DBHelper.COLUMN_USER_ID}=?", arrayOf(userID))
    }
}
