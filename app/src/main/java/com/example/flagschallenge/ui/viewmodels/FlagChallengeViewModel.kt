package com.example.flagschallenge.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flagschallenge.model.CountryList
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class FlagChallengeViewModel(val context: Context) : ViewModel() {

    var countryList = arrayListOf<CountryList.Question>()
  //  var countDownTimerUtility: CountDownTimerUtility? = null

    init {
        countryList = loadData() as ArrayList<CountryList.Question>

       /* countDownTimerUtility = CountDownTimerUtility(
            onTicking = { timer ->
                val (minute, sec) = Utility.convertSecondsToMS(timer.toInt())
//                binding?.tvTimer?.text = "$minute:$sec"
                countDownLastMinute = minute
                countDownLastSecond = sec
                mTimer.value = "$minute:$sec"
                Log.d("CntDwnTmerUtlty", "onTick timer-> $timer -> $minute:$sec")
            },
            onFinished = { ->
                mTimer.value = "Finished"
                Log.d("CntDwnTmerUtlty", "onFinish")
            }
        )*/
//        countDownTimerUtility?.startCountDownTimer(20L,1L)
    }


    fun loadData(): MutableList<CountryList.Question> {
        val fileInputStream = context.assets.open("MyData.json")
        val reader = BufferedReader(InputStreamReader(fileInputStream))
        val json = reader.use { it.readText() }

        val countryList = Gson().fromJson(json, CountryList::class.java)
        return countryList.questions
    }


}

class FlagChallengeViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FlagChallengeViewModel(context) as T
    }
}