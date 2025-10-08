package com.example.flagschallenge.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.flagschallenge.ParentActivity
import com.example.flagschallenge.R
import com.example.flagschallenge.databinding.ActivityFlagChallengeBinding
import com.example.flagschallenge.model.CountryList
import com.example.flagschallenge.ui.adapter.CountryNameAdapter
import com.example.flagschallenge.utility.ImageUtility
import com.example.flagschallenge.utility.Utility
import com.example.flagschallenge.ui.viewmodels.FlagChallengeViewModel
import com.example.flagschallenge.ui.viewmodels.FlagChallengeViewModelFactory
import kotlin.getValue

class FlagChallengeActivity : ParentActivity() {
    private var binding: ActivityFlagChallengeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFlagChallengeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val vm by lazy {
            ViewModelProvider(
                this@FlagChallengeActivity,
                FlagChallengeViewModelFactory(this@FlagChallengeActivity)
            )[FlagChallengeViewModel::class.java]
        }
        binding?.viewmodel = vm

        initialization()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

    }

    //    var countryList = arrayListOf<CountryList.Question>()
    private lateinit var countryNameAdapter: CountryNameAdapter

//    private lateinit var countDownTimerUtility: CountDownTimerUtility

    var questionIndex = 0
    private fun initialization() {

        preference.totalQuestion = binding?.viewmodel?.countryList!!.size

        if (intent.action == "START_NEXT_QUESTION") {
            questionIndex = intent.getIntExtra("NEXT_QUESTION_INDEX", 1)
            Log.i("loadData", "questionIndex=$questionIndex :: ${preference.challengeStarted}")
            binding?.clQuestion?.visibility = View.GONE
            binding?.llStartTimerAlert?.visibility = View.VISIBLE
            if (!preference.challengeStarted && questionIndex == 1) binding?.viewmodel?.countDownTimerUtility?.startCountDownTimer(
                20L,
                1L
            )
        }

        // load using coroutine but not need so commented
        //lifecycleScope.launch {
        /*lifecycleScope.launch(Dispatchers.IO) {
            countryList = binding?.viewmodel?.countryList!!
        }.join()*/

        Log.i("loadData", "2 countryList.size -> ${binding?.viewmodel?.countryList!!.size}")

//        countryNameAdapter = CountryNameAdapter(countryList[0].countries)
        countryNameAdapter = CountryNameAdapter(arrayListOf())
        binding?.rvCountryList?.run {
            layoutManager = GridLayoutManager(this@FlagChallengeActivity, 2)
            adapter = countryNameAdapter
            totalQuestion = binding?.viewmodel?.countryList!!.size
        }

        countryNameAdapter.onItemClick = { pos ->
            userSelectPos = pos
            userSelectCountryId =
                binding?.viewmodel?.countryList!![currentQuestion].countries[pos].id

            /*for (i in 0 until binding?.viewmodel?.countryList!![currentQuestion].countries.size) {
                binding?.viewmodel?.countryList!![currentQuestion].countries[pos].userSelect =
                    i == userSelectPos
            }*/



            countryNameAdapter.updateAnswer(userSelectPos = pos)
        }

        binding?.viewmodel?.mTimer?.observe(this, { timer ->
            if (timer.equals("Finished")) {
                if (!preference.challengeStarted) {
                    preference.challengeStarted = true
                    binding?.clQuestion?.visibility = View.VISIBLE
                    binding?.llStartTimerAlert?.visibility = View.GONE
                    loadNextQuestion()
                } else {
                    isResultShown = if (!isResultShown) {
                        countryNameAdapter.updateAnswer(
                            binding?.viewmodel?.countryList!![currentQuestion].answer_id,
                            userSelectPos
                        )

                        if (binding?.viewmodel?.countryList!![currentQuestion].answer_id == userSelectCountryId) {
                            preference.correctAnswer += 1
                        }

                        binding?.viewmodel?.countDownTimerUtility?.startCountDownTimer(10L, 1L)/*if (userSelectCountryId == binding?.viewmodel?.countryList!![currentQuestion].answer_id) {
                            binding?.viewmodel?.countryList!![currentQuestion].countries[userSelectPos].userSelect =
                                true
                            binding?.viewmodel?.countryList!![currentQuestion].countries[userSelectPos].correctAnswer =
                                true
                            countryNameAdapter.notifyItemChanged(userSelectPos)
                        } else {
                            countryNameAdapter.updateAnswer(binding?.viewmodel?.countryList!![currentQuestion].answer_id)
                        }*/
                        true
                    } else {
                        // binding?.viewmodel?.countDownTimerUtility?.startCountDownTimer(30L, 1L)
                        loadNextQuestion()
                        false
                    }
                }
                //preference.challengeStarted != preference.challengeStarted
            } else {
                if (!preference.challengeStarted) {
                    binding?.tvTimer?.text = "$timer"

                } else {
                    binding?.timerDisplay?.visibility = View.VISIBLE
                    binding?.timerDisplay?.text = "$timer"
                }
            }
        })

        binding?.tvChallengeBack?.setOnClickListener {
            val intent = Intent(this@FlagChallengeActivity, SetTimerActivity::class.java)
            startActivity(intent)
            finish()
        }

        /*  countDownTimerUtility = CountDownTimerUtility(
              onTicking = { timer ->
                  val (minute, sec) = Utility.convertSecondsToMS(timer.toInt())
                  binding?.tvTimer?.text = "$minute:$sec"

                  Log.d("CntDwnTmerUtlty", "onTick timer-> $timer -> $minute:$sec")
              },
              onFinished = { ->
                  Log.d("CntDwnTmerUtlty", "onFinish")
              }
          )
          lifecycleScope.launch {
              delay(1000)
  //            countDownTimerUtility.startCountDownTimer(20L,1L)
              binding?.viewmodel?.countDownTimerUtility?.startCountDownTimer(20L,1L)
          }
  */

        //}
    }

    private var totalQuestion = -1
    private var currentQuestion = -1
    private var isResultShown = false
    private var userSelectPos = -1
    private var userSelectCountryId = -1
    private fun loadNextQuestion() {
        Log.i(
            "loadNextQuestion", "1 totalQuestion=$totalQuestion :: currentQuestion=$currentQuestion"
        )
        currentQuestion++
        if (currentQuestion < totalQuestion) {
            userSelectPos = -1
            userSelectCountryId = -1

            Log.i(
                "loadNextQuestion",
                "2 totalQuestion=$totalQuestion :: currentQuestion=$currentQuestion"
            )

            ImageUtility.loadImagineGlide(
                Utility.getCountryFlagByCode(binding?.viewmodel?.countryList!![currentQuestion].country_code),
                R.drawable.ic_launcher_background,
                binding?.imgCountryFlag
            )
            countryNameAdapter.updateNextList(binding?.viewmodel?.countryList!![currentQuestion].countries)
            binding?.tvQuestionNo?.text = "${currentQuestion + 1}"
            binding?.viewmodel?.countDownTimerUtility?.startCountDownTimer(30L, 1L)
        } else {

            binding?.tvChallengeScore?.visibility = View.GONE
            binding?.clQuestion?.visibility = View.GONE
            binding?.llAnswerView?.visibility = View.VISIBLE

            binding?.tvChallengeScore?.text =
                "Your score is ${preference.correctAnswer}/${preference.totalQuestion}"

        }

    }

}