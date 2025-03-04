package com.zybooks.workway.repository

import android.content.ContentValues
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.zybooks.workway.database.DBHelper
import com.zybooks.workway.model.Message
import java.time.LocalDateTime

class MessageRepository(context: Context) {
    private val dbHelper = DBHelper(context)

    // Insert Message
    fun insertMessage(message: Message): Long {
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_SENDER_ID, message.senderID)
            put(DBHelper.COLUMN_RECEIVER_ID, message.recipientID)
            put(DBHelper.COLUMN_MESSAGE_CONTENT, message.content)
            put(DBHelper.COLUMN_MESSAGE_TIMESTAMP, System.currentTimeMillis())
        }
        return dbHelper.insertData(DBHelper.TABLE_MESSAGES, values)
    }

    // Read Messages
    @RequiresApi(Build.VERSION_CODES.O)
    fun getMessages(): List<Message> {
        val messageList = mutableListOf<Message>()
        val cursor = dbHelper.getData(DBHelper.TABLE_MESSAGES, arrayOf(DBHelper.COLUMN_MESSAGE_ID, DBHelper.COLUMN_SENDER_ID, DBHelper.COLUMN_RECEIVER_ID, DBHelper.COLUMN_MESSAGE_CONTENT, DBHelper.COLUMN_MESSAGE_TIMESTAMP))

        while (cursor.moveToNext()) {
            val message = Message().apply {
                messageID = cursor.getString(0)
                senderID = cursor.getString(1)
                recipientID = cursor.getString(2)
                content = cursor.getString(3)
                timestamp = LocalDateTime.now()  // Replace with correct conversion
            }
            messageList.add(message)
        }
        cursor.close()
        return messageList
    }

    // Delete Message
    fun deleteMessage(messageID: String): Int {
        return dbHelper.deleteData(DBHelper.TABLE_MESSAGES, "${DBHelper.COLUMN_MESSAGE_ID}=?", arrayOf(messageID))
    }
}
