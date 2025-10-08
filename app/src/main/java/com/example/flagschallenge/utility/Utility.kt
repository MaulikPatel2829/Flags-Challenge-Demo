package com.example.flagschallenge.utility

import com.example.flagschallenge.R

object Utility {

    fun getCountryFlagByCode(countryCode: String): Int {
        return when (countryCode) {
            "NZ" -> R.drawable.ic_nz
            "AW" -> R.drawable.ic_aw
            "EC" -> R.drawable.ic_ec
            "PY" -> R.drawable.ic_py
            "KG" -> R.drawable.ic_kg
            "PM" -> R.drawable.ic_pm
            "JP" -> R.drawable.ic_jp
            "TM" -> R.drawable.ic_tm
            "GA" -> R.drawable.ic_ga
            "MQ" -> R.drawable.ic_mq
            "BZ" -> R.drawable.ic_bz
            "CZ" -> R.drawable.ic_cz
            "AE" -> R.drawable.ic_ae
            "JE" -> R.drawable.ic_je
            "LS" -> R.drawable.ic_ls
            else -> R.drawable.ic_nz
        }
    }

    fun convertSecondsToHoursMinutes(totalSeconds: Int): Pair<Int, Int> {
        val hours = totalSeconds / 3600 // 1 hour = 3600 seconds
        val remainingSecondsAfterHours = totalSeconds % 3600
        val minutes = remainingSecondsAfterHours / 60 // 1 minute = 60 seconds
        return Pair(hours, minutes)
    }

    fun convertSecondsToMS(totalSeconds: Int): Pair<Int, Int> {
        val hours = totalSeconds / 3600
        val remainingSecondsAfterHours = totalSeconds % 3600
        val minutes = remainingSecondsAfterHours / 60
        val seconds = remainingSecondsAfterHours % 60
        return Pair( minutes, seconds)
    }

    fun convertSecondsToHMS(totalSeconds: Int): Triple<Int, Int, Int> {
        val hours = totalSeconds / 3600
        val remainingSecondsAfterHours = totalSeconds % 3600
        val minutes = remainingSecondsAfterHours / 60
        val seconds = remainingSecondsAfterHours % 60
        return Triple(hours, minutes, seconds)
    }
}