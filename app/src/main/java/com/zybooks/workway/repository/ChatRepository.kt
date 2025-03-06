package com.zybooks.workway.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.zybooks.workway.database.DBHelper
import com.zybooks.workway.model.Chat
import com.zybooks.workway.model.Message
import com.zybooks.workway.model.User
import java.text.SimpleDateFormat
import java.util.Locale

class ChatRepository(context: Context) {
    private val dbHelper = DBHelper(context)
    val userRepo = UserRepository(context)

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

    fun getAllChats(): Pair<List<Chat>, List<User>> {

        val chats = mutableListOf<Chat>()
        val users = mutableListOf<User>()
        val cursor: Cursor = dbHelper.getData(
            DBHelper.TABLE_CHATS,
            arrayOf(DBHelper.COLUMN_CHAT_ID, DBHelper.COLUMN_USER1_ID, DBHelper.COLUMN_USER2_ID
                , DBHelper.COLUMN_CHAT_CREATED_AT, DBHelper.COLUMN_CHAT_LAST_MESSAGE),
            orderBy = "created_at DESC"
        )
        while (cursor.moveToNext()) {
            val chat = Chat().apply {
                chatID = cursor.getInt(0)
                senderUser1ID = cursor.getInt(1)
                receiverUser2ID = cursor.getInt(2)
                createdAt = cursor.getString(3)
                lastMessage = cursor.getString(4)
            }
            users.add(userRepo.getUser(chat.senderUser1ID))
            chats.add(chat)
        }

        cursor.close()
        return Pair(chats, users)
    }
}
