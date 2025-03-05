package com.zybooks.workway

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Outline
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import android.widget.TextView
import androidx.annotation.GravityInt
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.marginTop
import androidx.core.view.setPadding

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
                Triple("Mandy", "About the meeting later...", R.drawable.abed),
                Triple("Harry", "Good luck in the meeting later dude...", R.drawable.grace),
                Triple("Stamos", "Welcome to the team Mark, I really want to start with...", R.drawable.abed),
                Triple("Stamos", "Welcome to the team Mark, I really want to start with...", R.drawable.antony),
                Triple("Stamos", "Welcome to the team Mark, I really want to start with...", R.drawable.mark),
                Triple("Stamos", "Welcome to the team Mark, I really want to start with...", R.drawable.sarah),
                Triple("Stamos", "Welcome to the team Mark, I really want to start with...", R.drawable.susan),
                Triple("Stamos", "Welcome to the team Mark, I really want to start with...", R.drawable.abed),
                Triple("Raul", "I don't want to alarm you, but I think our...", R.drawable.johnface)
            )

            for (person in peopleMessages) {
                val newMessageBox = RelativeLayout(activity, null, 0, R.style.messagesBox).apply {
                    layoutParams = RelativeLayout.LayoutParams(dpToPx(375, context), RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                        topMargin = dpToPx(10, context)
                        leftMargin = dpToPx(17, context)
                        setPadding(dpToPx(10, context), dpToPx(10, context), dpToPx(10, context), dpToPx(10, context))
                    }
                    isClickable = true
                    isFocusable = true

                    outlineProvider = ViewOutlineProvider.BACKGROUND
                    clipToOutline = true
                    setBackgroundColor(ContextCompat.getColor(context, R.color.darkblue_transparent))

                    val radius = dpToPx(16, context).toFloat()
                    val border = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = radius  // Set corner radius
                        setStroke(dpToPx(2, context), ContextCompat.getColor(context, R.color.black))  // Set border width and color
                        setColor(ContextCompat.getColor(context, R.color.darkblue_transparent))  // Set background color
                    }
                    background = border
                    outlineProvider = object : ViewOutlineProvider() {
                        override fun getOutline(view: View?, outline: Outline?) {
                            outline?.setRoundRect(0, 0, width, height, radius)
                        }
                    }
                }

                newMessageBox.setOnClickListener {
                    val intent = Intent(activity, MessageHistoryActivity::class.java)
                    intent.putExtra("USER_NAME", person.first)
                    startActivity(intent)
                }

                val imageView = ImageView(activity, null, 0, R.style.messagesImage).apply {
                    id = View.generateViewId()
                    setImageResource(person.third)
                    layoutParams = RelativeLayout.LayoutParams(dpToPx(70, context), dpToPx(70, context)).apply {
                        rightMargin = dpToPx(10, context)
                    }
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

                val title = TextView(activity, null, 0, R.style.messagesTitle)
                title.text = person.first
                val messageDescription = TextView(activity, null, 0, R.style.messagesDescription)
                messageDescription.text = person.second

                textLayout.addView(title)
                textLayout.addView(messageDescription)

                newMessageBox.addView(imageView)
                newMessageBox.addView(textLayout)

                parentLayout.addView(newMessageBox)
            }

            return rootView
        }
    }
