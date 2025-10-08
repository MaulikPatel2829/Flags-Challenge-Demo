package com.example.flagschallenge.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.flagschallenge.ParentActivity
import com.example.flagschallenge.R
import com.example.flagschallenge.databinding.ActivityFlagChallengeBinding
import com.example.flagschallenge.ui.adapter.CountryNameAdapter
import com.example.flagschallenge.utility.ImageUtility
import com.example.flagschallenge.utility.Utility
import com.example.flagschallenge.ui.viewmodels.FlagChallengeViewModel
import com.example.flagschallenge.ui.viewmodels.FlagChallengeViewModelFactory
import com.example.flagschallenge.utility.CountDownTimerUtility
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

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {

        if (intent.action == "START_FIRST_QUESTION") {
            questionIndex = intent.getIntExtra("NEXT_QUESTION_INDEX", 1)

            binding?.clQuestion?.visibility = View.GONE
            binding?.llStartTimerAlert?.visibility = View.VISIBLE
            if (!preference.challengeStarted && questionIndex == 1) {
                countDownTimerUtility?.startCountDownTimer(
                    20L, 1L
                )
            }
            intent.removeExtra("START_FIRST_QUESTION")
        }

        if (intent.getBooleanExtra("load_question", false)) {
            val targetIndex = intent.getIntExtra("NEXT_QUESTION_INDEX", 1)
            countDownTimerUtility?.cancelCountDownTimer()
            currentQuestion = targetIndex
            isChallengeActive = true
            loadNextQuestion()
            intent.removeExtra("load_question")
        }

    }

    private lateinit var countryNameAdapter: CountryNameAdapter


    var questionIndex = 0
    var countDownTimerUtility: CountDownTimerUtility? = null
    private fun initialization() {

        preference.totalQuestion = binding?.viewmodel?.countryList!!.size

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

            countryNameAdapter.updateAnswer(userSelectPos = pos)
        }

        countDownTimerUtility = CountDownTimerUtility(onTicking = { timer ->
            val (minute, sec) = Utility.convertSecondsToMS(timer.toInt())

            if (!preference.challengeStarted) {
                binding?.tvTimer?.text = "$minute:$sec"

            } else {
                binding?.timerDisplay?.visibility = View.VISIBLE
                binding?.timerDisplay?.text = "$minute:$sec"
            }

        }, onFinished = { ->
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

                    countDownTimerUtility?.startCountDownTimer(10L, 1L)
                    true
                } else {

                    loadNextQuestion()
                    false
                }
            }

        })

        binding?.tvChallengeBack?.setOnClickListener {
            val intent = Intent(this@FlagChallengeActivity, SetTimerActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private var totalQuestion = -1
    private var currentQuestion = -1
    private var isResultShown = false
    private var isChallengeActive = false
    private var userSelectPos = -1
    private var userSelectCountryId = -1
    private fun loadNextQuestion() {

        currentQuestion++
        if (currentQuestion < totalQuestion) {
            userSelectPos = -1
            userSelectCountryId = -1

            ImageUtility.loadImagineGlide(
                Utility.getCountryFlagByCode(binding?.viewmodel?.countryList!![currentQuestion].country_code),
                R.drawable.ic_launcher_background,
                binding?.imgCountryFlag
            )
            countryNameAdapter.updateNextList(binding?.viewmodel?.countryList!![currentQuestion].countries)
            binding?.tvQuestionNo?.text = "${currentQuestion + 1}"
            countDownTimerUtility?.startCountDownTimer(30L, 1L)

            /*
             // used for schedule to load in background even app closed
             FlagsChallengeScheduler.scheduleNextQuestion(
                 applicationContext, currentQuestion + 1, 40L,
             )*/
        } else {

            binding?.timerDisplay?.visibility = View.GONE
            binding?.clQuestion?.visibility = View.GONE
            binding?.llAnswerView?.visibility = View.VISIBLE
            preference.challengeStarted = false
            binding?.tvChallengeScore?.text =
                "${preference.correctAnswer}/${preference.totalQuestion}"

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimerUtility?.cancelCountDownTimer()
    }

}