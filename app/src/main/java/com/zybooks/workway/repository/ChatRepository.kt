package com.zybooks.workway.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.zybooks.workway.database.DBHelper
import com.zybooks.workway.model.Chat
import com.zybooks.workway.model.Message
import java.text.SimpleDateFormat
import java.util.Locale

class ChatRepository(context: Context) {
    private val dbHelper = DBHelper(context)

    fun createChat(user1Id: Int, user2Id: Int): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("user1_id", user1Id)
            put("user2_id", user2Id)
        }
        return db.insert("chats", null, values)
    }

    fun sendMessage(chatId: Int, senderId: Int, messageText: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("chat_id", chatId)
            put("sender_id", senderId)
            put("message_text", messageText)
        }
        return db.insert("messages", null, values)
    }

    fun getMessages(chatId: Int): List<Message> {

        val messages = mutableListOf<Message>()
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())


        val cursor: Cursor = dbHelper.getData(
            DBHelper.TABLE_MESSAGES,
            arrayOf(DBHelper.COLUMN_MESSAGE_ID, DBHelper.COLUMN_CHAT_ID_KEY, DBHelper.COLUMN_SENDER_ID, DBHelper.COLUMN_MESSAGE_CONTENT, DBHelper.COLUMN_MESSAGE_TIMESTAMP),
            "chat_id = ?",
            arrayOf(chatId.toString()),
            "timestamp ASC"
        )

        while (cursor.moveToNext()) {
            val message = Message().apply {
                messageID = cursor.getInt(0)
                chatID = cursor.getInt(1)
                senderID = cursor.getInt(2)
                messageText = cursor.getString(3)
                timestamp = sdf.format(cursor.getLong(4))
            }
            messages.add(message)
        }
        cursor.close()
        return messages
    }
}
