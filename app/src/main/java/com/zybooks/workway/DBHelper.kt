package com.zybooks.workway.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USERS_TABLE)
        db.execSQL(CREATE_MESSAGES_TABLE)
        db.execSQL(CREATE_MEETINGS_TABLE)
        db.execSQL(CREATE_CALENDAR_EVENTS_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    companion object{
        const val DATABASE_NAME = "app_database"
        const val DATABASE_VERSION = 1

        // Users Table
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_NAME = "name"
        const val COLUMN_USER_EMAIL = "email"
        const val COLUMN_USER_PASSWORD = "password"

        // Messages Table
        const val TABLE_MESSAGES = "messages"
        const val COLUMN_MESSAGE_ID = "id"
        const val COLUMN_SENDER_ID = "sender_id"
        const val COLUMN_RECEIVER_ID = "receiver_id"
        const val COLUMN_MESSAGE_CONTENT = "content"
        const val COLUMN_MESSAGE_TIMESTAMP = "timestamp"

        // Meetings Table
        const val TABLE_MEETINGS = "meetings"
        const val COLUMN_MEETING_ID = "id"
        const val COLUMN_MEETING_TITLE = "title"
        const val COLUMN_MEETING_ORGANIZER_ID = "organizer_id"
        const val COLUMN_MEETING_STARTTIME = "start_time"
        const val COLUMN_MEETING_ENDTIME = "end_time"
        const val COLUMN_MEETING_DESCRIPTION = "description"

        // Calendar Events Table
        const val TABLE_CALENDAR_EVENTS = "calendar_events"
        const val COLUMN_EVENT_ID = "id"
        const val COLUMN_EVENT_USER_ID = "user_id"
        const val COLUMN_EVENT_TITLE = "event_title"
        const val COLUMN_EVENT_STARTDATE = "event_start_date"
        const val COLUMN_EVENT_ENDDATE = "event_end_date"
        const val COLUMN_EVENT_DESCRIPTION = "event_description"

        // Table Creation Queries
        private const val CREATE_USERS_TABLE = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_NAME TEXT NOT NULL,
                $COLUMN_USER_EMAIL TEXT UNIQUE NOT NULL,
                $COLUMN_USER_PASSWORD TEXT NOT NULL
            )
        """

        private const val CREATE_MESSAGES_TABLE = """
            CREATE TABLE $TABLE_MESSAGES (
                $COLUMN_MESSAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SENDER_ID INTEGER NOT NULL,
                $COLUMN_RECEIVER_ID INTEGER NOT NULL,
                $COLUMN_MESSAGE_CONTENT TEXT NOT NULL,
                $COLUMN_MESSAGE_TIMESTAMP INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_SENDER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID),
                FOREIGN KEY ($COLUMN_RECEIVER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """

        private const val CREATE_MEETINGS_TABLE = """
            CREATE TABLE $TABLE_MEETINGS (
                $COLUMN_MEETING_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_MEETING_TITLE TEXT NOT NULL,
                $COLUMN_MEETING_DESCRIPTION TEXT NOT NULL,
                $COLUMN_MEETING_ORGANIZER_ID INTEGER NOT NULL,
                $COLUMN_MEETING_STARTTIME INTEGER NOT NULL,
                $COLUMN_MEETING_ENDTIME INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_MEETING_ORGANIZER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """

        private const val CREATE_CALENDAR_EVENTS_TABLE = """
            CREATE TABLE $TABLE_CALENDAR_EVENTS (
                $COLUMN_EVENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EVENT_USER_ID INTEGER NOT NULL,
                $COLUMN_EVENT_TITLE TEXT NOT NULL,
                $COLUMN_EVENT_STARTDATE INTEGER NOT NULL,
                $COLUMN_EVENT_ENDDATE INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_EVENT_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """
    }

    fun insertDummyData() {
        val db = this.writableDatabase

        // Dummy Users
        val user1 = ContentValues().apply {
            put(DBHelper.COLUMN_USER_NAME, "Alice")
            put(DBHelper.COLUMN_USER_EMAIL, "alice@example.com")
            put(DBHelper.COLUMN_USER_PASSWORD, "password123")
        }
        val user2 = ContentValues().apply {
            put(DBHelper.COLUMN_USER_NAME, "Bob")
            put(DBHelper.COLUMN_USER_EMAIL, "bob@example.com")
            put(DBHelper.COLUMN_USER_PASSWORD, "securepass")
        }
        db.insert(DBHelper.TABLE_USERS, null, user1)
        db.insert(DBHelper.TABLE_USERS, null, user2)

        // Dummy Meetings
        val meeting1 = ContentValues().apply {
            put(DBHelper.COLUMN_MEETING_TITLE, "Project Discussion")
            put(DBHelper.COLUMN_MEETING_DESCRIPTION, "Discuss project updates")
            put(DBHelper.COLUMN_MEETING_ORGANIZER_ID, 1)
            put(DBHelper.COLUMN_MEETING_STARTTIME, System.currentTimeMillis())
            put(DBHelper.COLUMN_MEETING_ENDTIME, System.currentTimeMillis() + 3600000)
        }
        db.insert(DBHelper.TABLE_MEETINGS, null, meeting1)

        // Dummy Calendar Event
        val event1 = ContentValues().apply {
            put(DBHelper.COLUMN_EVENT_USER_ID, 1)
            put(DBHelper.COLUMN_EVENT_TITLE, "Team Meeting")
            put(DBHelper.COLUMN_EVENT_STARTDATE, System.currentTimeMillis())
            put(DBHelper.COLUMN_EVENT_ENDDATE, System.currentTimeMillis() + 7200000)
            put(DBHelper.COLUMN_EVENT_DESCRIPTION, "Weekly team sync-up")
        }
        db.insert(DBHelper.TABLE_CALENDAR_EVENTS, null, event1)

        db.close()
    }

    // Insert Function
    fun insertData(tableName: String, values: ContentValues): Long {
        val db = this.writableDatabase
        val result = db.insert(tableName, null, values)
        db.close()
        return result
    }

    // Read Function
    fun getData(tableName: String, columns: Array<String>, selection: String? = null, selectionArgs: Array<String>? = null): Cursor {
        val db = this.readableDatabase
        return db.query(tableName, columns, selection, selectionArgs, null, null, null)
    }

    // Update Function
    fun updateData(tableName: String, values: ContentValues, whereClause: String, whereArgs: Array<String>): Int {
        val db = this.writableDatabase
        val result = db.update(tableName, values, whereClause, whereArgs)
        db.close()
        return result
    }

    // Delete Function
    fun deleteData(tableName: String, whereClause: String, whereArgs: Array<String>): Int {
        val db = this.writableDatabase
        val result = db.delete(tableName, whereClause, whereArgs)
        db.close()
        return result
    }

}