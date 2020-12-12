package com.example.shopick.activities

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shopick.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_settings.*


class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        val acct: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            val personName: String? = acct.displayName
            val personGivenName: String? = acct.givenName
            val personFamilyName: String? = acct.familyName
            val personEmail: String? = acct.email
            val personId: String? = acct.id
            val personPhoto: Uri? = acct.photoUrl
            Picasso.get().load(personPhoto.toString()).into(ImageProfile)
            if (personName != null) {
                UsernameProfile.text = "WELCOME " + personName.toUpperCase()
            }
            if (personEmail != null) {
                EmailProfile.text = personEmail.toString()
            }
            if (personId != null) {
                UserIDProfile.text =personId.toString()
            }
        }
        // profile act close
        backButton.setOnClickListener{
            finish()
        }

    }
}