package uz.gita.my2048puzzleumidjon.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log

class MySharedPref private constructor() {
    private lateinit var myPreferences: SharedPreferences
    private lateinit var editor: Editor

    private constructor(context: Context) : this() {
        myPreferences = context.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE)
        editor = myPreferences.edit()
    }

    companion object {
        private val MY_PREFS = "my_prefs"
        private val INSTANCE_STATE = "instance_state"
        private val HIGHEST_SCORE_STATE = "highest_score_state"
        private val CURRENT_SCORE_STATE = "current_score_state"
        private lateinit var instance: MySharedPref

        fun init(context: Context) {
            if (!(Companion::instance.isInitialized)) {
                instance = MySharedPref(context)
            }
        }

        fun getInstance(): MySharedPref {
            return instance
        }
    }

    fun saveCurrentScore(score: Int) {
        editor.putInt(CURRENT_SCORE_STATE, score).apply()
    }

    fun restoreCurrentScore(): Int {
        return myPreferences.getInt(CURRENT_SCORE_STATE, 0)
    }

    fun saveHighestScore(highest: Int) {
        editor.putInt(HIGHEST_SCORE_STATE, highest).apply()
    }

    fun restoreHighestScore(): Int {
        return myPreferences.getInt(HIGHEST_SCORE_STATE, 0)
    }

    fun saveGameState(sb: StringBuilder) {
        editor.putString(INSTANCE_STATE, sb.toString()).apply()
        Log.d("GGG:Save", sb.toString())
    }

    fun restoreGameState(): String? {
        Log.d("GGG:Restore", myPreferences.getString(INSTANCE_STATE, null).toString())
        return myPreferences.getString(INSTANCE_STATE, null)
    }

    fun nullify() {
        editor.remove(INSTANCE_STATE)
        editor.remove(CURRENT_SCORE_STATE)
        editor.apply()
    }
}