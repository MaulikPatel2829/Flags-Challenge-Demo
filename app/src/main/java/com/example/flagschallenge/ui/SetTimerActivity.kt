package com.example.flagschallenge.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.widget.addTextChangedListener
import com.example.flagschallenge.ParentActivity
import com.example.flagschallenge.R
import com.example.flagschallenge.databinding.ActivityMainBinding
import com.example.flagschallenge.services.AlarmReceiver
import com.example.flagschallenge.utility.BorderSpan
import java.util.Calendar

class SetTimerActivity : ParentActivity() {

    private lateinit var binding: ActivityMainBinding
//    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setSpannableString()
        initialization()
    }

    private lateinit var editTexts: Array<EditText>
    private fun initialization() {

        if (preference.challengeStarted){
            val intent = Intent(this, FlagChallengeActivity::class.java)
            intent.action = "START_NEXT_QUESTION"
            if (preference.currentQuestionNo != -1)
            {
                intent.putExtra("NEXT_QUESTION_INDEX", preference.currentQuestionNo)
            }
            intent.putExtra("load_question", true)
            intent.putExtra("is_restarted", true)
            startActivity(intent)
        }

        binding.buttonSave.setOnClickListener {
            Log.d("buttonSave", "time -> $hr1$hr2:$mn1$mn2:$sec1$sec2")
            if (hr1.isEmpty() || hr2.isEmpty() || mn1.isEmpty() || mn2.isEmpty() ||
                sec1.isEmpty() || sec2.isEmpty()
            ) {
                Toast.makeText(this@SetTimerActivity, "Please set proper time", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val time = buildString {
                append(binding.inputHourOne.text)
                append(binding.inputHourTwo.text)
                append(":")
                append(binding.inputMinuteOne.text)
                append(binding.inputMinuteTwo.text)
                append(":")
                append(binding.inputSecondOne.text)
                append(binding.inputSecondTwo.text)
            }
            Log.d("buttonSave", "time -> $time")
            preference.eventTime = time

            preference.eventTimeHour =
                binding.inputHourOne.text.toString() + "" + binding.inputHourTwo.text.toString()
            preference.eventTimeMinute =
                binding.inputMinuteOne.text.toString() + "" + binding.inputMinuteTwo.text.toString()
            preference.eventTimeSecond =
                binding.inputSecondOne.text.toString() + "" + binding.inputSecondTwo.text.toString()

            Log.d("buttonSave", "time -> ${preference.eventTimeHour.toInt()}:" +
                    "${preference.eventTimeMinute.toInt()}:${preference.eventTimeSecond.toInt()}")
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, preference.eventTimeHour.toInt())
            calendar.set(Calendar.MINUTE, preference.eventTimeMinute.toInt())
            calendar.set(Calendar.SECOND, preference.eventTimeSecond.toInt())

            preference.challengeStarted = false
            preference.correctAnswer = 0

            setAlarm(calendar)

        }
        textChangeEvents()
    }


    private var hr1 = ""
    private var hr2 = ""
    private var mn1 = ""
    private var mn2 = ""
    private var sec1 = ""
    private var sec2 = ""

    private fun textChangeEvents() {

        /*editTexts = arrayOf(
            binding.inputHourOne,
            binding.inputHourTwo,
            binding.inputMinuteOne,
            binding.inputMinuteTwo,
            binding.inputSecondOne,
            binding.inputSecondTwo
        )
        editTexts.forEach { et ->
            et.addTextChangedListener(createWatcher(et))
        }*/

        /* binding.inputHourOne.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                val timeSelected = SimpleDateFormat("HH:mm:ss").format(cal.time)
                Log.d("buttonSave", "timeSelected -> $timeSelected")
//                binding.inputHourOne.setText(SimpleDateFormat("HH:mm:ss").format(cal.time))
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }*/
        binding.inputHourOne.addTextChangedListener { afterTextChanged ->

            if (afterTextChanged?.isNotEmpty()!!) {
                if (afterTextChanged.toString().toInt() > 2) {
                    binding.inputHourOne.setText("")
                } else binding.inputHourTwo.requestFocus()
            } else {
                binding.inputHourOne.requestFocus()
            }

            hr1 = afterTextChanged.toString()
        }
        binding.inputHourTwo.addTextChangedListener { afterTextChanged ->
            if (afterTextChanged?.isNotEmpty()!!) {
                if (hr1.isNotEmpty()) {
                    if (hr1.toInt() == 2 && afterTextChanged.toString()
                            .isNotEmpty() && afterTextChanged.toString().toInt() > 4
                    ) {
                        binding.inputHourTwo.setText("")
                        binding.inputHourTwo.requestFocus()
                    } else if (afterTextChanged?.isNotEmpty()!!) {
                        binding.inputMinuteOne.requestFocus()
                    } else {
                        binding.inputHourOne.requestFocus()
                    }
                } else {
                    if (afterTextChanged?.isNotEmpty()!!) {
                        binding.inputHourTwo.setText("0")
                        binding.inputMinuteOne.requestFocus()
                    } else {
                        binding.inputHourOne.requestFocus()
                    }
                }
                hr1 = binding.inputHourOne.text.toString()
                hr2 = afterTextChanged.toString()
            } else {
                binding.inputHourOne.requestFocus()
                binding.inputHourOne.setSelection(binding.inputHourOne.text.length)
            }

        }
        binding.inputMinuteOne.addTextChangedListener { afterTextChanged ->
            if (afterTextChanged?.isNotEmpty()!!) {
                if (hr1.isEmpty() && hr2.isEmpty()) {
                    binding.inputHourOne.setText("0")
                    binding.inputHourTwo.setText("0")
                    if (afterTextChanged.isNotEmpty()) {
                        binding.inputMinuteTwo.requestFocus()
                    } else {
                        binding.inputHourTwo.requestFocus()
                    }
                } else {
                    if (hr1.toInt() == 2 && afterTextChanged.toString()
                            .isNotEmpty() && afterTextChanged.toString().toInt() > 6
                    ) {
                        binding.inputMinuteOne.setText("0")
                    } else if (afterTextChanged?.isNotEmpty()!!) {
                        binding.inputMinuteTwo.requestFocus()
                    } else {
                        binding.inputHourTwo.requestFocus()
                    }
                }
                hr1 = binding.inputHourOne.text.toString()
                hr2 = binding.inputHourTwo.text.toString()
                mn1 = afterTextChanged.toString()
            } else {
                binding.inputHourTwo.requestFocus()
                binding.inputHourTwo.setSelection(binding.inputHourOne.text.length)
            }

        }
        binding.inputMinuteTwo.addTextChangedListener { afterTextChanged ->

            if (afterTextChanged?.isNotEmpty()!!) {
                if (mn1.isNotEmpty()) {
                    if (mn1.toInt() == 6 && afterTextChanged.toString()
                            .isNotEmpty() && afterTextChanged.toString().toInt() > 0
                    ) {
                        binding.inputMinuteTwo.setText("0")
                    } else if (afterTextChanged?.isNotEmpty()!!) {
                        binding.inputSecondOne.requestFocus()
                    } else {
                        binding.inputMinuteOne.requestFocus()
                    }
                } else if (afterTextChanged.isNotEmpty()) {
                    binding.inputSecondOne.requestFocus()
                } else {
                    binding.inputMinuteOne.requestFocus()
                }
                hr1 = binding.inputHourOne.text.toString()
                hr2 = binding.inputHourTwo.text.toString()
                mn1 = binding.inputMinuteOne.text.toString()
                mn2 = afterTextChanged.toString()
            } else {
                binding.inputMinuteOne.requestFocus()
                binding.inputMinuteOne.setSelection(binding.inputHourOne.text.length)
            }
        }
        binding.inputSecondOne.addTextChangedListener { afterTextChanged ->
            if (afterTextChanged?.isNotEmpty()!!) {
                if (mn1.isEmpty() && mn2.isEmpty()) {
                    binding.inputMinuteOne.setText("0")
                    binding.inputMinuteTwo.setText("0")
                    if (afterTextChanged.isNotEmpty()) {
                        binding.inputSecondTwo.requestFocus()
                    } else {
                        binding.inputMinuteTwo.requestFocus()
                    }
                } else {
                    if (afterTextChanged?.isNotEmpty()!! && afterTextChanged.toString()
                            .toInt() > 6
                    ) {
                        binding.inputSecondOne.setText("0")
                        binding.inputSecondOne.requestFocus()
                    } else if (afterTextChanged.isNotEmpty()) {
                        binding.inputSecondTwo.requestFocus()
                    } else {
                        binding.inputMinuteTwo.requestFocus()
                    }
                }
                hr1 = binding.inputHourOne.text.toString()
                hr2 = binding.inputHourTwo.text.toString()
                mn1 = binding.inputMinuteOne.text.toString()
                mn2 = binding.inputMinuteTwo.text.toString()
                sec1 = afterTextChanged.toString()
            } else {
                binding.inputMinuteTwo.requestFocus()
                binding.inputMinuteTwo.setSelection(binding.inputHourOne.text.length)
            }

        }
        binding.inputSecondTwo.addTextChangedListener { afterTextChanged ->
            if (afterTextChanged?.isNotEmpty()!!) {
                if (sec1.isNotEmpty()) {
                    if (afterTextChanged?.isNotEmpty()!! && afterTextChanged.toString()
                            .toInt() > 6
                    ) {
                        binding.inputSecondTwo.setText("0")
                        binding.inputSecondTwo.requestFocus()
                    } else if (afterTextChanged.isNotEmpty()) {
                        binding.inputSecondTwo.requestFocus()
                    } else {
                        binding.inputSecondOne.requestFocus()
                    }

                } else {
                    if (afterTextChanged?.isNotEmpty()!!) {
                        binding.inputSecondTwo.requestFocus()
                    } else {
                        binding.inputSecondOne.requestFocus()
                    }
                }
                hr1 = binding.inputHourOne.text.toString()
                hr2 = binding.inputHourTwo.text.toString()
                mn1 = binding.inputMinuteOne.text.toString()
                mn2 = binding.inputMinuteTwo.text.toString()
                sec1 = binding.inputSecondOne.text.toString()
                sec2 = afterTextChanged.toString()
            } else {
                binding.inputSecondOne.requestFocus()
                binding.inputSecondOne.setSelection(binding.inputHourOne.text.length)
            }
        }

    }

    private fun createWatcher(currentEt: EditText): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val currentIndex = editTexts.indexOf(currentEt)
                if (s?.length == 1 && currentIndex < editTexts.size - 1) {
                    editTexts[currentIndex + 1].requestFocus()
                } else if (s?.isEmpty()!! && before == 1 && currentIndex > 0) {
                    editTexts[currentIndex - 1].requestFocus()
                }
            }

        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(calendar: Calendar) {
        Log.d("buttonSave", "setAlarm - time -> ${calendar.timeInMillis} - ${calendar.get(Calendar.HOUR_OF_DAY)}" +
                " - ${calendar.get(Calendar.MINUTE)} - ${calendar.get(Calendar.SECOND)}")
        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "START_FIRST_QUESTION"
        intent.putExtra("NEXT_QUESTION_INDEX", 1)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0,
            intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarm.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            pendingIntent
        )
        Toast.makeText(this, "Alarm has been setuped, now wait for its action", Toast.LENGTH_SHORT)
            .show()
        binding.inputSecondTwo.setText("")
        binding.inputSecondOne.setText("")
        binding.inputMinuteTwo.setText("")
        binding.inputMinuteOne.setText("")
        binding.inputHourTwo.setText("")
        binding.inputHourOne.setText("")
        binding.inputHourOne.requestFocus()
//        Log.d("buttonSave", "Alarm has been setuped, now wait for its action")
    }

    private fun setSpannableString() {
        val mainText = resources.getString(R.string.lbl_challenge_schedule)
        val stringToModify = "SCHEDULE"

        //val spannable = SpannableString(mainText)
        val spannableBuilder = SpannableStringBuilder(mainText)

        val startIndex = mainText.indexOf(stringToModify)
        val endIndex = startIndex + stringToModify.length

        spannableBuilder.setSpan(
            mainText, startIndex, endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableBuilder.setSpan(
            BorderSpan(R.color.strokeColor, 2f, 2f),
            startIndex, endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.subtitleChallengeSchedule.text = spannableBuilder

    }


}