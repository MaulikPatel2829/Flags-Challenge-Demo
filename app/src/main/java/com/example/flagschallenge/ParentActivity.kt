package com.example.flagschallenge

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.flagschallenge.preference.SharedPreference
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
open class ParentActivity: AppCompatActivity() {

    @Inject
    lateinit var preference : SharedPreference

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setStatusBarColor()
    }
    @SuppressLint("ObsoleteSdkInt")
    fun setStatusBarColor(statusBarColor: Int = R.color.orange) {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                val window = window
                // clear FLAG_TRANSLUCENT_STATUS flag:
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                // finally change the color
                window.statusBarColor = ContextCompat.getColor(applicationContext, statusBarColor)

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

}