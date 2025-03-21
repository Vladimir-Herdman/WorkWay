package com.zybooks.workway.repository

import android.content.ContentValues
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.zybooks.workway.database.DBHelper
import com.zybooks.workway.model.CalendarEvent
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

class CalendarRepository(context: Context) {
    private val dbHelper = DBHelper(context)

    // Insert Calendar Event
    fun insertEvent(event: CalendarEvent): Long {
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_EVENT_USER_ID, event.organizerID)
            put(DBHelper.COLUMN_EVENT_TITLE, event.title)
            put(DBHelper.COLUMN_EVENT_STARTDATE, event.startTime.toString())
            put(DBHelper.COLUMN_EVENT_ENDDATE, event.endTime.toString())
            put(DBHelper.COLUMN_EVENT_DESCRIPTION, event.description)
        }
        return dbHelper.insertData(DBHelper.TABLE_CALENDAR_EVENTS, values)
    }

    // Read Calendar Events
    fun getCalendarEvents(): List<CalendarEvent> {
        val eventList = mutableListOf<CalendarEvent>()
        val cursor = dbHelper.getData(DBHelper.TABLE_CALENDAR_EVENTS, arrayOf(DBHelper.COLUMN_EVENT_ID, DBHelper.COLUMN_EVENT_USER_ID, DBHelper.COLUMN_EVENT_TITLE, DBHelper.COLUMN_EVENT_STARTDATE, DBHelper.COLUMN_EVENT_ENDDATE, DBHelper.COLUMN_EVENT_DESCRIPTION))
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

        while (cursor.moveToNext()) {
            val event = CalendarEvent().apply {
                eventID = cursor.getString(0)
                organizerID = cursor.getString(1)
                title = cursor.getString(2)
                startTime = sdf.format(Date(cursor.getLong(3)))
                endTime =  sdf.format(Date(cursor.getLong(4)))
                description = cursor.getString(5)
            }
            eventList.add(event)
        }
        cursor.close()
        return eventList
    }

    // Delete Calendar Event
    fun deleteEvent(eventID: String): Int {
        return dbHelper.deleteData(DBHelper.TABLE_CALENDAR_EVENTS, "${DBHelper.COLUMN_EVENT_ID}=?", arrayOf(eventID))
    }
}
