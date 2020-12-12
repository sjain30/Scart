package com.example.shopick.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopick.R
import com.example.shopick.adapters.RecommendationAdapter
import kotlinx.android.synthetic.main.activity_scanned_item.*

class ScannedItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanned_item)

        recyclerViewRecommended.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RecommendationAdapter(arrayListOf("Apple","Mango"))
        recyclerViewRecommended.adapter = adapter
    }
}