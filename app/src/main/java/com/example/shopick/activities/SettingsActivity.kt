package com.example.shopick.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shopick.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlin.math.log


class SettingsActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // google account info fetch
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            val personName: String? = account.displayName
            val personGivenName: String? = account.givenName
            val personEmail: String? = account.email
            val personPhoto: Uri? = account.photoUrl
            Picasso.get().load(personPhoto.toString()).into(profiImage)
            if (personName != null) {
                usernameTV.text = "WELCOME " + personName.toUpperCase()
            }
        }

        // open preferences
        settingPref.setOnClickListener {
            val intent=Intent(this, SettingsViewPreferences::class.java)
            startActivity(intent)
        }

        // my profile activity
        ProfileKhol.setOnClickListener {
            val intent=Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }


        // google logout

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        logout.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val intent = Intent(this, GoogleLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // exit settings activity using back arror button

        backSettings.setOnClickListener {
            finish()
        }

    }
}