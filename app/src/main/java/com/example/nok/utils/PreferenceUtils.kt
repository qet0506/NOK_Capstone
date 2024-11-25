package com.example.nok.utils

import android.content.Context

object PreferenceUtils {

    private const val PREFS_NAME = "AppStatePrefs"
    private const val KEY_IS_EXAMPLE = "isExample"

    // 상태 저장 함수
    fun saveState(context: Context, isExample: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_EXAMPLE, isExample)
        editor.apply() // 비동기로 저장
    }

    // 상태 불러오기 함수
    fun loadState(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_IS_EXAMPLE, true) // 기본값은 true
    }

    fun saveString(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun loadString(context: Context, key: String, defaultValue: String = ""): String {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}
