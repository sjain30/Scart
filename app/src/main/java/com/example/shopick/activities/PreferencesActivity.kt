package com.example.shopick.activities

import android.content.Intent
import android.graphics.ColorSpace
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.viewpager.widget.ViewPager
import com.example.shopick.PreferenceModel.ModelPreferences
import com.example.shopick.R
import com.example.shopick.adapters.PreferenceAdapter
import kotlinx.android.synthetic.main.activity_google_login.*
import kotlinx.android.synthetic.main.activity_preferences.*

class PreferencesActivity : AppCompatActivity() {

    private lateinit var action: ActionBar

    private lateinit var  modelList: ArrayList<ModelPreferences>
    private lateinit var  preferenceAdapter: PreferenceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        next.setOnClickListener {
            val intent= Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

            loadPreferenceCards()

            ViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {

                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            })


    }

    private fun loadPreferenceCards() {
        modelList= ArrayList()
        modelList.add(ModelPreferences("Are you a Vegetarian or a Non-Vegetarian?", "Vegetarian" , "Non-Vegetarian"))
        modelList.add(ModelPreferences("Are you Lactose Intolerant?","Yes","No"))
        modelList.add(ModelPreferences("Eggs, Milk, Fish, Soy or Wheat?", "Yes","No"))
        modelList.add(ModelPreferences("What do you usually buy?", "Packaged Products","Groceries"))

        // adapter setup
        preferenceAdapter= PreferenceAdapter(this, modelList)
        ViewPager.adapter=preferenceAdapter
        ViewPager.setPadding(50,0,50,0)
        dots_indicator.setViewPager(ViewPager)
    }
}