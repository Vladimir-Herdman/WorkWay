package com.zybooks.workway

import android.graphics.Outline
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

data class Message(val text: String)

private fun getMessageHistory(userName: String?): ArrayList<Message> {
    var messageHistory = ArrayList<Message>()

    if (userName == "USER_NAME") {
        messageHistory.add(Message("How are you today?"))
        messageHistory.add(Message("I'm doing good today, how about you"))
        messageHistory.add(Message("I'm doing great, thanks for asking!"))
    } else {
        messageHistory.add(Message("This is a new message chain now unseen to bare eyes"))
        messageHistory.add(Message("If you can't handle the truth"))
        messageHistory.add(Message("Then stop seeking it out you lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard"))
        messageHistory.add(Message("Then stop seeking it out you lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard"))
        messageHistory.add(Message("Then stop seeking it out you lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard"))
        messageHistory.add(Message("Then stop seeking it out you lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard"))
        messageHistory.add(Message("Then stop seeking it out you lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard"))
        messageHistory.add(Message("Then stop seeking it out you lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard"))
        messageHistory.add(Message("Then stop seeking it out you lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard lard"))
    }

    return messageHistory
}

class MessageHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_message_history)

        val rootView = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val messagesContainer = findViewById<LinearLayout>(R.id.linearRoot)

        val userName = intent.getStringExtra("USER_NAME")
        val messages = getMessageHistory(userName)

        var messageSent = true
        for (message in messages) {
            var newMessageBox: RelativeLayout? = null
            if (messageSent) {
                newMessageBox = RelativeLayout(this, null, 0, R.style.messagesBoxRight).apply {
                    layoutParams = RelativeLayout.LayoutParams(dpToPx(250, context), RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                        topMargin = dpToPx(10, context)
                        setPadding(dpToPx(10, context), dpToPx(10, context), dpToPx(10, context), dpToPx(10, context))
                        gravity = Gravity.LEFT
                    }
                    isClickable = true
                    isFocusable = true

                    outlineProvider = ViewOutlineProvider.BACKGROUND
                    clipToOutline = true
                    setBackgroundColor(ContextCompat.getColor(context, R.color.darkblue_transparent))

                    val radius = dpToPx(16, context).toFloat()
                    outlineProvider = object : ViewOutlineProvider() {
                        override fun getOutline(view: View?, outline: Outline?) {
                            outline?.setRoundRect(0, 0, width, height, radius)
                        }
                    }
                }

            } else {
                //Message recieved, so put it on the left side
                newMessageBox = RelativeLayout(this, null, 0, R.style.messagesBoxLeft).apply {
                    layoutParams = RelativeLayout.LayoutParams(dpToPx(250, context), RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                        topMargin = dpToPx(10, context)
                        leftMargin = dpToPx(145, context)
                        gravity = Gravity.RIGHT
                        setPadding(dpToPx(10, context), dpToPx(10, context), dpToPx(10, context), dpToPx(10, context))
                    }
                    isClickable = true
                    isFocusable = true

                    outlineProvider = ViewOutlineProvider.BACKGROUND
                    clipToOutline = true
                    setBackgroundColor(ContextCompat.getColor(context, R.color.darkblue_transparent))

                    val radius = dpToPx(16, context).toFloat()
                    outlineProvider = object : ViewOutlineProvider() {
                        override fun getOutline(view: View?, outline: Outline?) {
                            outline?.setRoundRect(0, 0, width, height, radius)
                        }
                    }
                }
            }

            val messageText = TextView(this, null, 0, R.style.messagesText).apply {
                text = message.text
            }

            newMessageBox.addView(messageText)

            messagesContainer.addView(newMessageBox)
            messageSent = !messageSent
        }

        val entryBox = findViewById<EditText>(R.id.messagesInput)
        val sendButton = findViewById<ImageButton>(R.id.sendButton)
        sendButton.setOnClickListener {
            val message_text = entryBox.text.toString().trim()
            entryBox.text.clear()

            val newMessageBox = RelativeLayout(this, null, 0, R.style.messagesBoxLeft).apply {
                layoutParams = RelativeLayout.LayoutParams(dpToPx(250, context), RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                    topMargin = dpToPx(10, context)
                    leftMargin = dpToPx(145, context)
                    gravity = Gravity.RIGHT
                    setPadding(dpToPx(10, context), dpToPx(10, context), dpToPx(10, context), dpToPx(10, context))
                }
                isClickable = true
                isFocusable = true

                outlineProvider = ViewOutlineProvider.BACKGROUND
                clipToOutline = true
                setBackgroundColor(ContextCompat.getColor(context, R.color.darkblue_transparent))

                val radius = dpToPx(16, context).toFloat()
                outlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View?, outline: Outline?) {
                        outline?.setRoundRect(0, 0, width, height, radius)
                    }
                }
            }

            val messageText = TextView(this, null, 0, R.style.messagesText).apply {
                text = message_text
            }

            newMessageBox.addView(messageText)
            messagesContainer.addView(newMessageBox)
        }
    }
}