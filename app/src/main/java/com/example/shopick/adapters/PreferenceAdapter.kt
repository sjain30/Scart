package com.example.shopick.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.shopick.datamodels.ModelPreferences
import com.example.shopick.R
import com.example.shopick.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_preferences.view.*
import java.util.*
import java.util.logging.Handler
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class PreferenceAdapter(private val context: Context, private val ModelArrayList:ArrayList<ModelPreferences>):
    PagerAdapter() {

    private val mDatabaseReference = FirebaseUtil.getDatabase().getReference("Preferences")

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

        view.choice2.setBackgroundColor(Color.parseColor("#FEC63E"))

        // set the data here
        view.Preference.text=preference_quesion
        view.choice1.text=first_choice
        view.choice2.text= second_choice

        view.choice1.setOnClickListener {
            // load data to firebase
            view.choice1.setBackgroundColor(Color.GREEN)
            mDatabaseReference.child("${FirebaseAuth.getInstance().currentUser?.uid}").child(view.Preference.text.toString()).setValue(view.choice1.text.toString())
                .addOnSuccessListener {
                    Log.d("TAG", "addList: Success")
                    Timer().schedule(1000) {
                        view.choice1.setBackgroundColor(Color.parseColor("#FEC63E"))
                        view.choice2.isClickable = false
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "addList: ${it.toString()}")
                }
            view.choice2.isClickable=true
        }

        view.choice2.setOnClickListener {
            // load data to firebase
            view.choice2.setBackgroundColor(Color.GREEN)
            mDatabaseReference.child("${FirebaseAuth.getInstance().currentUser?.uid}").child(view.Preference.text.toString()).setValue(view.choice2.text.toString())
                .addOnSuccessListener {
                    Log.d("TAG", "addList: Success")
                    Timer().schedule(1000) {
                        view.choice2.setBackgroundColor(Color.parseColor("#FEC63E"))
                        view.choice1.isClickable = false
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "addList: ${it.toString()}")
                }
            view.choice1.isClickable=true
        }
        container.addView(view,position)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

}