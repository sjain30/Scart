package com.example.shopick.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.shopick.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_settings.*
import kotlin.math.log


class SettingsActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // google account info fetch
        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_home_back))
        actionBar!!.setDisplayHomeAsUpEnabled(true)

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

    }
}