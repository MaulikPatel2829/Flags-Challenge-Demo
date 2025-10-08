package com.example.flagschallenge.preference

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreference @Inject constructor (@param:ApplicationContext private val context: Context) {

    //    init {
    val preference: SharedPreferences = context.getSharedPreferences("FlagsChallenge", 0)
    val editor: SharedPreferences.Editor = preference.edit()
//    }

    fun save(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun save(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    fun save(key: String, value: Long) {
        editor.putLong(key, value)
        editor.apply()
    }
    fun save(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }


    fun getValueString(key: String): String? {
        return preference.getString(key, "")
    }
    fun getValueBoolean(key: String): Boolean? {
        return preference.getBoolean(key, false)
    }
    fun getValueInt(key: String): Int? {
        return preference.getInt(key, -1)
    }
    fun getValueLong(key: String): Long? {
        return preference.getLong(key, 0L)
    }
    object PrefKeys {
    const val eventTime = "eventTime"
    const val eventTimeHour = "eventTimeHour"
    const val eventTimeMinute = "eventTimeMinute"
    const val eventTimeSecond = "eventTimeSecond"

    const val challengeStarted = "challengeStarted"
    const val correctAnswer = "correctAnswer"
    const val totalQuestion = "totalQuestion"



    }


    var eventTime: String = ""
        get() {
            return getValueString(PrefKeys.eventTime)!!
        }
        set(value) {
            field = value
            save(PrefKeys.eventTime, value)
        }

    var eventTimeHour: String = ""
        get() {
            return getValueString(PrefKeys.eventTimeHour)!!
        }
        set(value) {
            field = value
            save(PrefKeys.eventTimeHour,value)
        }

    var eventTimeMinute: String = ""
        get(){
            return getValueString(PrefKeys.eventTimeMinute)!!
        }
        set(value) {
            field = value
            save(PrefKeys.eventTimeMinute, value)
        }

    var eventTimeSecond: String = ""
        get() {
            return getValueString(PrefKeys.eventTimeSecond)!!
        }
        set(value) {
            field = value
            save(PrefKeys.eventTimeSecond, value)
        }

    var correctAnswer: Int = 0
        get() {
            return getValueInt(PrefKeys.correctAnswer)!!
        }
        set(value) {
            field = value
            save(PrefKeys.correctAnswer, value)
        }

    var totalQuestion: Int = 0
        get() {
            return getValueInt(PrefKeys.totalQuestion)!!
        }
        set(value) {
            field = value
            save(PrefKeys.totalQuestion, value)
        }

    var challengeStarted: Boolean = false
        get() {
            return getValueBoolean(PrefKeys.challengeStarted)!!
        }
        set(value) {
            field = value
            save(PrefKeys.challengeStarted, value)
        }


}