package com.example.shopick.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


object SavedSharedPreference {

    private const val EMAIL = "email"
    private const val USERNAME = "username"

    private fun getSharedPreference(ctx: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }
    @JvmStatic
    private fun editor(context: Context, const: String, string: String) {
        getSharedPreference(context).edit().putString(const, string).apply()
    }

    @JvmStatic
    fun setEmail(context: Context, email: String) {
        editor(context, EMAIL, email)
    }

    @JvmStatic
    fun getEmail(context: Context) = getSharedPreference(context).getString(EMAIL, "")

    @JvmStatic
    fun setUsername(context: Context, username: String) {
        editor(context, USERNAME, username)
    }

    @JvmStatic
    fun getUsername(context: Context) = getSharedPreference(context).getString(USERNAME, "")
}