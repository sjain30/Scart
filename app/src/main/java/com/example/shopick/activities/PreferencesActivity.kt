package com.example.shopick.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.viewpager.widget.ViewPager
import com.example.shopick.datamodels.ModelPreferences
import com.example.shopick.R
import com.example.shopick.adapters.PreferenceAdapter
import kotlinx.android.synthetic.main.activity_preferences.*

class PreferencesActivity : AppCompatActivity() {

    private lateinit var action: ActionBar
//    private val mDatabaseReference = FirebaseUtil.getDatabase().getReference("Preferences")
    private lateinit var  modelList: ArrayList<ModelPreferences>
    private lateinit var  preferenceAdapter: PreferenceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

            loadPreferenceCards()
        var positionOfCards=ViewPager.currentItem
            ViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    positionOfCards=ViewPager.currentItem
//                    next.setOnClickListener {
//                        positionOfCards+=1
//                        ViewPager.currentItem=positionOfCards
//                    }
                }

                override fun onPageSelected(position: Int) {
                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            })
// handle swipe left with next button
        next.setOnClickListener {
            positionOfCards = positionOfCards+1
            ViewPager.currentItem=positionOfCards
            Log.d("ViewPagerCurrentItem","${ViewPager.currentItem}")
                    if (positionOfCards==modelList.size-1){
                        next.setOnClickListener {
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
            }
//            previous.setOnClickListener{
//                positionOfCards= positionOfCards-1
//                ViewPager.currentItem=positionOfCards
//                Log.d("ViewPagerPreviousItem","${ViewPager.currentItem}")
//            }



// handle swipe right with previous button
//        previous.setOnClickListener{
//            ViewPager.currentItem = ViewPager.currentItem-1
//            Log.d("ViewPagerPreviousItem","${ViewPager.currentItem}")
//        }

//        while (ViewPager.currentItem<=modelList.size-1){
//            choice1.setOnClickListener {
//                // load data to firebase
//                mDatabaseReference.child("${FirebaseAuth.getInstance().currentUser?.uid}").child(Preference.text.toString()).setValue(choice1.text.toString())
//                    .addOnSuccessListener {
//                        Log.d("TAG", "addList: Success")
//                    }
//                    .addOnFailureListener {
//                        Log.d("TAG", "addList: ${it.toString()}")
//                    }
//            }
//            choice2.setOnClickListener {
//                // load data to firebase
//                mDatabaseReference.child("${FirebaseAuth.getInstance().currentUser?.uid}").child(Preference.text.toString()).setValue(choice2.text.toString())
//                    .addOnSuccessListener {
//                        Log.d("TAG", "addList: Success")
//                    }
//                    .addOnFailureListener {
//                        Log.d("TAG", "addList: ${it.toString()}")
//                    }
//            }

    }

    private fun loadPreferenceCards() {
        modelList= ArrayList()
        modelList.add(ModelPreferences("Are you a Vegetarian or a Non-Vegetarian?", "Vegetarian" , "Non-Vegetarian"))
        modelList.add(ModelPreferences("Are you Lactose Intolerant?","Yes","No"))
        modelList.add(ModelPreferences("Allergic to Eggs, Milk, Fish, Soy or Wheat?", "Yes","No"))
        modelList.add(ModelPreferences("What do you usually buy?", "Packaged Products","Groceries"))

        // adapter setup
        preferenceAdapter= PreferenceAdapter(this, modelList)
        ViewPager.adapter=preferenceAdapter
        ViewPager.setPadding(50,0,50,0)
        dots_indicator.setViewPager(ViewPager)
    }

}