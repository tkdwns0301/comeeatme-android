package com.hand.comeeatme.data.preference

import android.content.Context
import android.content.SharedPreferences

class AppPreferenceManager(
    private val context: Context,
) {
    companion object {
        const val PREFERENCES_NAME = "comeeatme_app_pref"
        private const val DEFAULT_VALUE_STRING = ""
        private const val DEFAULT_VALUE_BOOLEAN = false
        private const val DEFAULT_VALUE_INT = -1
        private const val DEFAULT_VALUE_LONG = -1L
        private const val DEFAULT_VALUE_FLOAT = -1f

        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val MEMBER_ID = "MEMBER_ID"
    }

    private val recentSearchList = ArrayList<String>()

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private val prefs by lazy { getPreferences(context)}
    private val editor by lazy { prefs.edit()}

    fun clear() {
        editor.clear()
        editor.apply()
    }

    fun putAccessToken(accessToken: String) {
        editor.putString(ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun putRefreshToken(refreshToken: String) {
        editor.putString(REFRESH_TOKEN, refreshToken)
        editor.apply()
    }

    fun getRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }

    fun putMemberId(memberId: Int) {
        editor.putInt(MEMBER_ID, memberId)
        editor.apply()
    }

    fun getMemberId(): Int? {
        return prefs.getInt(MEMBER_ID, 0)
    }


}