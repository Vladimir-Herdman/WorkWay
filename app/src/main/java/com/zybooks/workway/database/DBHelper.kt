package com.zybooks.workway.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zybooks.workway.model.Company
import com.zybooks.workway.repository.CompanyRepository

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USERS_TABLE)
        db.execSQL(CREATE_MESSAGES_TABLE)
        db.execSQL(CREATE_MEETINGS_TABLE)
        db.execSQL(CREATE_CALENDAR_EVENTS_TABLE)
        db.execSQL(CREATE_COMPANY_TABLE)
        db.execSQL(CREATE_CHAT_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    companion object{
        const val DATABASE_NAME = "Workway_db"
        const val DATABASE_VERSION = 1

        // Users Table
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_NAME = "name"
        const val COLUMN_USER_EMAIL = "email"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_USER_BUSINESS_CODE = "business_code"

        // Messages Table
        const val TABLE_MESSAGES = "messages"
        const val COLUMN_MESSAGE_ID = "id"
        const val COLUMN_SENDER_ID = "sender_id"
        const val COLUMN_CHAT_ID_KEY = "chat_id"
        const val COLUMN_MESSAGE_CONTENT = "message_text"
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


        //Company Table

        const val TABLE_COMPANY = "companies"
        const val COLUMN_COMPANY_ID = "id"
        const val COLUMN_COMPANY_TITLE = "title"
        const val COLUMN_COMPANY_LOGO = "logo"
        const val COLUMN_COMPANY_DESCRIPTION = "description"
        const val COLUMN_COMPANY_BUSINESS_CODE = "business_code"
        const val COLUMN_COMPANY_DOMAIN = "domain"

        // Chat Table
        const val TABLE_CHATS = "chats"
        const val COLUMN_CHAT_ID = "chat_id"
        const val COLUMN_USER1_ID = "user1_id"
        const val COLUMN_USER2_ID = "user2_id"
        const val COLUMN_CHAT_CREATED_AT = "created_at"



        // Table Creation Queries
        private const val CREATE_USERS_TABLE = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_NAME TEXT NOT NULL,
                $COLUMN_USER_EMAIL TEXT UNIQUE NOT NULL,
                $COLUMN_USER_PASSWORD TEXT NOT NULL,
                $COLUMN_USER_BUSINESS_CODE TEXT UNIQUE NOT NULL
            )
        """

        private const val CREATE_MESSAGES_TABLE = """
            CREATE TABLE $TABLE_MESSAGES (
                $COLUMN_MESSAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SENDER_ID INTEGER NOT NULL,
                $COLUMN_CHAT_ID_KEY INTEGER NOT NULL,
                $COLUMN_MESSAGE_CONTENT TEXT NOT NULL,
                $COLUMN_MESSAGE_TIMESTAMP INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_SENDER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID),
                FOREIGN KEY ($COLUMN_CHAT_ID_KEY) REFERENCES $TABLE_CHATS($COLUMN_CHAT_ID)
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
                $COLUMN_EVENT_DESCRIPTION TEXT NOT NULL,
                FOREIGN KEY ($COLUMN_EVENT_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """

        private const val CREATE_COMPANY_TABLE = """
        CREATE TABLE $TABLE_COMPANY (
            $COLUMN_COMPANY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_COMPANY_TITLE TEXT NOT NULL,
            $COLUMN_COMPANY_LOGO TEXT NOT NULL,
            $COLUMN_COMPANY_DESCRIPTION TEXT NOT NULL,
            $COLUMN_COMPANY_BUSINESS_CODE TEXT NOT NULL,
            $COLUMN_COMPANY_DOMAIN TEXT UNIQUE NOT NULL
        )
    """

        private const val CREATE_CHAT_TABLE = """
    CREATE TABLE $TABLE_CHATS (
        $COLUMN_CHAT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COLUMN_USER1_ID INTEGER NOT NULL,
        $COLUMN_USER2_ID INTEGER NOT NULL,
        $COLUMN_CHAT_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY ($COLUMN_USER1_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID),
        FOREIGN KEY ($COLUMN_USER2_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
    )
"""

    }

    fun insertDummyData() {
        val db = this.writableDatabase

        // Dummy Users
        val user1 = ContentValues().apply {
            put(COLUMN_USER_NAME, "Alice")
            put(COLUMN_USER_EMAIL, "alice@onu.edu")
            put(COLUMN_USER_PASSWORD, "password123")
            put(COLUMN_USER_BUSINESS_CODE, "ONU123")
        }
        val user2 = ContentValues().apply {
            put(COLUMN_USER_NAME, "Bob")
            put(COLUMN_USER_EMAIL, "bob@onu.edu")
            put(COLUMN_USER_PASSWORD, "password456")
            put(COLUMN_USER_BUSINESS_CODE, "ONU123")
        }
        db.insert(TABLE_USERS, null, user1)
        db.insert(TABLE_USERS, null, user2)

        // Dummy Meetings
        val meeting1 = ContentValues().apply {
            put(COLUMN_MEETING_TITLE, "Project Discussion")
            put(COLUMN_MEETING_DESCRIPTION, "Discuss project updates")
            put(COLUMN_MEETING_ORGANIZER_ID, 1)
            put(COLUMN_MEETING_STARTTIME, System.currentTimeMillis())
            put(COLUMN_MEETING_ENDTIME, System.currentTimeMillis() + 3600000)
        }
        val meeting2 = ContentValues().apply {
            put(COLUMN_MEETING_TITLE, "Client Meeting")
            put(COLUMN_MEETING_DESCRIPTION, "Discuss project requirements with client")
            put(COLUMN_MEETING_ORGANIZER_ID, 2)
            put(COLUMN_MEETING_STARTTIME, System.currentTimeMillis() + 7200000)
            put(COLUMN_MEETING_ENDTIME, System.currentTimeMillis() + 10800000)
        }
        val meeting3 = ContentValues().apply {
            put(COLUMN_MEETING_TITLE, "Design Review")
            put(COLUMN_MEETING_DESCRIPTION, "Review UI/UX designs")
            put(COLUMN_MEETING_ORGANIZER_ID, 1)
            put(COLUMN_MEETING_STARTTIME, System.currentTimeMillis() + 14400000)
            put(COLUMN_MEETING_ENDTIME, System.currentTimeMillis() + 18000000)
        }
        db.insert(TABLE_MEETINGS, null, meeting1)
        db.insert(TABLE_MEETINGS, null, meeting2)
        db.insert(TABLE_MEETINGS, null, meeting3)

        // Dummy Calendar Events
        val event1 = ContentValues().apply {
            put(COLUMN_EVENT_USER_ID, 1)
            put(COLUMN_EVENT_TITLE, "Team Meeting")
            put(COLUMN_EVENT_STARTDATE, System.currentTimeMillis())
            put(COLUMN_EVENT_ENDDATE, System.currentTimeMillis() + 7200000)
            put(COLUMN_EVENT_DESCRIPTION, "Weekly team sync-up")
        }
        val event2 = ContentValues().apply {
            put(COLUMN_EVENT_USER_ID, 2)
            put(COLUMN_EVENT_TITLE, "Conference")
            put(COLUMN_EVENT_STARTDATE, System.currentTimeMillis() + 86400000)
            put(COLUMN_EVENT_ENDDATE, System.currentTimeMillis() + 90000000)
            put(COLUMN_EVENT_DESCRIPTION, "Tech conference in NYC")
        }
        val event3 = ContentValues().apply {
            put(COLUMN_EVENT_USER_ID, 1)
            put(COLUMN_EVENT_TITLE, "Code Review")
            put(COLUMN_EVENT_STARTDATE, System.currentTimeMillis() + 43200000)
            put(COLUMN_EVENT_ENDDATE, System.currentTimeMillis() + 46800000)
            put(COLUMN_EVENT_DESCRIPTION, "Review code with team")
        }
        val event4 = ContentValues().apply {
            put(COLUMN_EVENT_USER_ID, 2)
            put(COLUMN_EVENT_TITLE, "Hackathon")
            put(COLUMN_EVENT_STARTDATE, System.currentTimeMillis() + 172800000)
            put(COLUMN_EVENT_ENDDATE, System.currentTimeMillis() + 180000000)
            put(COLUMN_EVENT_DESCRIPTION, "Participating in AI Hackathon")
        }
        db.insert(TABLE_CALENDAR_EVENTS, null, event1)
        db.insert(TABLE_CALENDAR_EVENTS, null, event2)
        db.insert(TABLE_CALENDAR_EVENTS, null, event3)
        db.insert(TABLE_CALENDAR_EVENTS, null, event4)

        // Dummy Chats
        val chat1 = ContentValues().apply {
            put(COLUMN_USER1_ID, 1)
            put(COLUMN_USER2_ID, 2)
        }
        val chat2 = ContentValues().apply {
            put(COLUMN_USER1_ID, 2)
            put(COLUMN_USER2_ID, 1)
        }
        val chat1Id = db.insert(TABLE_CHATS, null, chat1)
        val chat2Id = db.insert(TABLE_CHATS, null, chat2)

        // Dummy Messages for Chat 1 (5 messages)
        for (i in 1..5) {
            val message = ContentValues().apply {
                put(COLUMN_CHAT_ID_KEY, chat1Id)
                put(COLUMN_SENDER_ID, if (i % 2 == 0) 1 else 2)
                put(COLUMN_MESSAGE_CONTENT, "Message $i in Chat 1")
                put(COLUMN_MESSAGE_TIMESTAMP, System.currentTimeMillis())
            }
            db.insert(TABLE_MESSAGES, null, message)
        }

        // Dummy Messages for Chat 2 (6 messages)
        for (i in 1..6) {
            val message = ContentValues().apply {
                put(COLUMN_CHAT_ID_KEY, chat2Id)
                put(COLUMN_SENDER_ID, if (i % 2 == 0) 2 else 1)
                put(COLUMN_MESSAGE_CONTENT, "Message $i in Chat 2")
                put(COLUMN_MESSAGE_TIMESTAMP, System.currentTimeMillis())
            }
            db.insert(TABLE_MESSAGES, null, message)
        }

        db.close()
    }


    //insert dummy companies

    fun insertDummyCompanies(context: Context) {
        val companyRepository = CompanyRepository(context)

        val companies = listOf(
            Company().apply {
                title = "Ohio Northern University"
                logo = "onu_logo.png"
                description = "A private university in Ohio."
                businessCode = "ONU123"
                domain = "onu.edu"
            },
            Company().apply {
                title = "Ohio State University"
                logo = "osu_logo.png"
                description = "A public research university in Ohio."
                businessCode = "OSU456"
                domain = "osu.edu"
            },
            Company().apply {
                title = "IBM"
                logo = "ibm_logo.png"
                description = "A multinational technology company."
                businessCode = "IBM789"
                domain = "ibm.com"
            }
        )

        for (company in companies) {
            companyRepository.insertCompany(company)
        }
    }

    // Insert Function
    fun insertData(tableName: String, values: ContentValues): Long {
        val db = this.writableDatabase
        val result = db.insert(tableName, null, values)
        db.close()
        return result
    }

    // Read Function
    fun getData(
        tableName: String,
        columns: Array<String>,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        orderBy: String? = null // Optional ORDER BY clause
    ): Cursor {
        val db = this.readableDatabase
        return db.query(tableName, columns, selection, selectionArgs, null, null, orderBy)
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

    fun getTableColumns(tableName: String): List<String> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("PRAGMA table_info($tableName);", null)
        val columnNames = mutableListOf<String>()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                columnNames.add(cursor.getString(1)) // Column index 1 = column name
            }
            cursor.close()
        }
        return columnNames
    }

}