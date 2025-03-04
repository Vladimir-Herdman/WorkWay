package com.zybooks.workway.repository

import android.content.ContentValues
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.zybooks.workway.database.DBHelper
import com.zybooks.workway.model.Meeting
import java.time.LocalDateTime

class MeetingRepository(context: Context) {
    private val dbHelper = DBHelper(context)

    // Insert Meeting
    fun insertMeeting(meeting: Meeting): Long {
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_MEETING_TITLE, meeting.title)
            put(DBHelper.COLUMN_MEETING_DESCRIPTION, meeting.description)
            put(DBHelper.COLUMN_MEETING_ORGANIZER_ID, meeting.organizerID)
            put(DBHelper.COLUMN_MEETING_STARTTIME, meeting.startTime.toString())
            put(DBHelper.COLUMN_MEETING_ENDTIME, meeting.endTime.toString())
        }
        return dbHelper.insertData(DBHelper.TABLE_MEETINGS, values)
    }

    // Read Meetings
    @RequiresApi(Build.VERSION_CODES.O)
    fun getMeetings(): List<Meeting> {
        val meetingList = mutableListOf<Meeting>()
        val cursor = dbHelper.getData(DBHelper.TABLE_MEETINGS, arrayOf(DBHelper.COLUMN_MEETING_ID, DBHelper.COLUMN_MEETING_TITLE, DBHelper.COLUMN_MEETING_DESCRIPTION, DBHelper.COLUMN_MEETING_ORGANIZER_ID, DBHelper.COLUMN_MEETING_STARTTIME, DBHelper.COLUMN_MEETING_ENDTIME))

        while (cursor.moveToNext()) {
            val meeting = Meeting().apply {
                meetingID = cursor.getString(0)
                title = cursor.getString(1)
                description = cursor.getString(2)
                organizerID = cursor.getString(3)
                startTime = LocalDateTime.now()  // Replace with proper parsing
                endTime = LocalDateTime.now()
            }
            meetingList.add(meeting)
        }
        cursor.close()
        return meetingList
    }

    // Delete Meeting
    fun deleteMeeting(meetingID: String): Int {
        return dbHelper.deleteData(DBHelper.TABLE_MEETINGS, "${DBHelper.COLUMN_MEETING_ID}=?", arrayOf(meetingID))
    }
}
