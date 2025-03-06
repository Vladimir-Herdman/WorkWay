package com.zybooks.workway

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.core.view.children

class Training : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_training, container, false)

        val grid = rootview.findViewById<GridLayout>(R.id.light_grid)
        for (trainBox in grid.children) {
            trainBox.setOnClickListener {
                val intent = Intent(activity, TrainingActivity::class.java)
                startActivity(intent)
            }
        }

        return rootview
    }
}
