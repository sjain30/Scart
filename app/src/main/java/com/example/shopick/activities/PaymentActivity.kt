package com.example.shopick.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopick.R
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val total = intent.getIntExtra("amount",0)

        amt.text = "₹$total.00"

        backHome.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }
}