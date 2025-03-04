package com.zybooks.workway

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.GravityInt

fun dpToPx(dp: Int, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

    class Messages : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_messages, container, false)

            // Find the parent layout from the fragment's root view
            val parentLayout = rootView.findViewById<LinearLayout>(R.id.messagesParent)
            // Example array of data taht could be in database, create messages off of it
            val peopleMessages: Array<Triple<String, String, Int>> = arrayOf(
                Triple("John", "Hey, how have you been lately...", R.drawable.johnface),
                Triple("Mandy", "About the meeting later...", R.drawable.johnface),
                Triple("Harry", "Good luck in the meeting later dude...", R.drawable.johnface),
                Triple("Stamos", "Welcome to the team Mark, I really awant to start with...", R.drawable.johnface),
                Triple("Raul", "I don't want to alarm you, but I think our...", R.drawable.johnface)
            )

            for (person in peopleMessages) {
                val newMessageBox = RelativeLayout(activity).apply {
                    layoutParams = LinearLayout.LayoutParams(dpToPx(350, context), RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    setBackgroundResource(R.color.darkblue_transparent)
                }

                val imageView = ImageView(activity).apply {
                    id = View.generateViewId()
                    layoutParams = RelativeLayout.LayoutParams(dpToPx(70, context), dpToPx(70, context)).apply {
                        addRule(RelativeLayout.ALIGN_PARENT_START)
                    }
                    setImageResource(person.third)
                    setBackgroundResource(R.drawable.circle)
                }

                val textLayout = LinearLayout(activity).apply {
                    orientation = LinearLayout.VERTICAL
                    layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        addRule(RelativeLayout.RIGHT_OF, imageView.id)
                        addRule(RelativeLayout.CENTER_VERTICAL)
                    }
                }

                val title = TextView(activity).apply {
                    text = person.first
                    setTextColor(resources.getColor(android.R.color.white))
                }
                val messageDescription = TextView(activity).apply {
                    text = person.second
                    setTextColor(resources.getColor(android.R.color.white))
                }

                textLayout.addView(title)
                textLayout.addView(messageDescription)

                newMessageBox.addView(imageView)
                newMessageBox.addView(textLayout)

                parentLayout.addView(newMessageBox)
            }

            return rootView
        }
    }
