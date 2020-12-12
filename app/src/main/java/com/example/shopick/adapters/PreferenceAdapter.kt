package com.example.shopick.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.viewpager.widget.PagerAdapter
import com.example.shopick.PreferenceModel.ModelPreferences
import com.example.shopick.R
import kotlinx.android.synthetic.main.item_preferences.view.*
import java.security.AccessControlContext

class PreferenceAdapter(private val context: Context, private val ModelArrayList:ArrayList<ModelPreferences>):
    PagerAdapter() {


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
       return view==`object`
    }

    override fun getCount(): Int {
       return ModelArrayList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // card layout for choices is inflated here
        val view = LayoutInflater.from(context).inflate(R.layout.item_preferences,container,false)
        // fetch data here
        val datamodel=ModelArrayList[position]
        val preference_quesion= datamodel.question
        val first_choice=datamodel.choice1
        val second_choice=datamodel.choice2

        // set the data here
        view.Preference.text=preference_quesion
        view.choice1.text=first_choice
        view.choice2.text= second_choice

        container.addView(view,position)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

}