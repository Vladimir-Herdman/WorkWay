package com.zybooks.workway

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import java.util.Calendar

class Meetings : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_meetings, container, false)

        val daysGrid = rootview.findViewById<GridLayout>(R.id.dayscontainer)

        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) // 0-based, so January = 0
        val currentYear = calendar.get(Calendar.YEAR)

        // Get the number of days in the current month
        val numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Get the starting day of the week for the first day of the month
        calendar.set(currentYear, currentMonth, 1)
        val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 1-based (Sunday = 1, Saturday = 7)

        val sizeInSp = 8f;
        val textSize16 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sizeInSp, getResources().getDisplayMetrics());

        // Clear the previous days (if any)
        daysGrid.removeAllViews()

        // Add empty cells for the days before the start of the month
        for (i in 1 until startDayOfWeek) {
            val emptyCell = TextView(requireContext())
            emptyCell.layoutParams = GridLayout.LayoutParams()
            daysGrid.addView(emptyCell)
        }

        // Add the days of the month to the grid
        for (day in 1..numDays) {
            val dayCell = TextView(requireContext())
            dayCell.text = day.toString()
            dayCell.textAlignment = View.TEXT_ALIGNMENT_CENTER
            dayCell.setPadding(dpToPx(16, requireContext()), dpToPx(16, requireContext()), dpToPx(16, requireContext()), dpToPx(16, requireContext()))
            dayCell.setTextColor(getResources().getColor(android.R.color.white)) // Set text color to white
            dayCell.setTextSize(textSize16) // Set text size

            // Set the layout params for each day
            val layoutParams = GridLayout.LayoutParams()
            layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT
            layoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED)
            layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED)

            dayCell.layoutParams = layoutParams

            // Add the TextView to the GridLayout
            daysGrid.addView(dayCell)
        }

        return rootview
    }
}