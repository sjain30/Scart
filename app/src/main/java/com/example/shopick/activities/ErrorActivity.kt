package com.example.shopick.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.example.shopick.R
import kotlinx.android.synthetic.main.activity_error.*

class ErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        CustomActivityOnCrash.getStackTraceFromIntent(intent)
        //getting crashing intent
        val config = CustomActivityOnCrash.getConfigFromIntent(intent)
        /**
         * If config is null or not getting an intent simply finish the app
         */
        if (config == null) {
            finish()
            return
        }
        if (config.isShowRestartButton && config.restartActivityClass != null) {
            restartApp.text = "Restart App"
            restartApp.setOnClickListener {
                CustomActivityOnCrash.restartApplication(
                    this,
                    config
                )
            }
        } else {
            restartApp.text = "Close App"
            restartApp.setOnClickListener {
                CustomActivityOnCrash.closeApplication(
                    this,
                    config
                )
            }
        }
    }
}