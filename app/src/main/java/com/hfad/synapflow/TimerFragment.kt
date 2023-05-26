package com.hfad.synapflow

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.work.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hfad.synapflow.PrefUtil.Companion.getPrevTimerLength
import com.hfad.synapflow.PrefUtil.Companion.getTimeRemaining
import me.zhanghai.android.materialprogressbar.MaterialProgressBar


class TimerFragment : Fragment() {
    enum class TimerState {
        Stop, Pause, Play
    }

    //setting up viewModel
    private val viewModel : TimerViewModel by activityViewModels()
    private lateinit var timer: CountDownTimer
    private lateinit var myContext: Context

    private  lateinit var play : FloatingActionButton
    private  lateinit var pause : FloatingActionButton
    private  lateinit var stop : FloatingActionButton
    private lateinit var progress_countdown: MaterialProgressBar
    private  lateinit var  countdown: TextView
    private  lateinit var  title: TextView

    val fsdb = FirestoreData()
    //--------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Timer | Synapflow"
        }
    //--------------------------------------------------------------------//
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_timer, container, false)
    }
    //--------------------------------------------------------------------//
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myContext = view.context
        play = view.findViewById<FloatingActionButton>(R.id.fab_play)
        pause = view.findViewById<FloatingActionButton>(R.id.fab_pause)
        stop = view.findViewById<FloatingActionButton>(R.id.fab_stop)
        progress_countdown = view.findViewById<MaterialProgressBar>(R.id.progress_countdown)
        countdown = view.findViewById<TextView>(R.id.countdown)
        title = view.findViewById<TextView>(R.id.timerTitle)


        //created a FirestoreData() object to be used to track each session (if needed anymore)


        updateButtons()
        updateCountdownUI()
        // Consider turning this into a function.
        if ((viewModel.secondsRemaining.value!! > 0) and (viewModel.timerState.value!! == TimerFragment.TimerState.Play)) {
            val elapsedTime = (System.currentTimeMillis() - viewModel.lastCurrentTime.value!!) / 1000
            val newSecondsRemaining = viewModel.secondsRemaining.value!! - elapsedTime

            // If the timer finished while off the screen
            if (newSecondsRemaining <= 0) {
                viewModel.secondsRemaining.setValue(0)
                updateCountdownUI()
                onTimerFinished()
            } else { // Restart the timer with the new time remaining
                viewModel.secondsRemaining.setValue(newSecondsRemaining)
                viewModel.timer.setValue(object : android.os.CountDownTimer((viewModel.secondsRemaining.value?: 0) * 1000, 1000) {
                    override fun onFinish() = onTimerFinished()
                    override fun onTick(millisUntilFinished: kotlin.Long) {
                        viewModel.lastCurrentTime.setValue(System.currentTimeMillis())
                        viewModel.secondsRemaining.setValue(millisUntilFinished / 1000)
                        updateCountdownUI()
                    }
                })
                viewModel.timer.value!!.start()
                progress_countdown.max = if (viewModel.onStudySession())  viewModel.studyTimerMax.value!!.toInt() else viewModel.breakTimerMax.value!!.toInt()
            }

        } else {
            progress_countdown.max = 1500
            progress_countdown.progress = 1499
        }

            //println(viewModel.timerState.value!!)
        // Initial setup
        play.setOnClickListener {
            viewModel.timerState.setValue(TimerFragment.TimerState.Play)
            startTimer()
        }
        pause.setOnClickListener {
            viewModel.timerState.setValue(TimerFragment.TimerState.Pause)
            updateButtons()
            viewModel.timer.value!!.cancel()
        }
        stop.setOnClickListener {
            viewModel.timerState.setValue(TimerFragment.TimerState.Stop)
            viewModel.timer.value!!.cancel()
            onTimerFinished()
        }
    }
    //--------------------------------------------------------------------//
    override fun onResume() {
        super.onResume()
        initTimer()
    }

    override fun onPause() {
        super.onPause()
        if (viewModel.timerState.value ==  TimerFragment.TimerState.Play){
            viewModel.timer.value!!.cancel()
        } else if (viewModel.timerState.value == TimerFragment.TimerState.Pause) {
            PrefUtil.setPrevTimerLength((viewModel.timerLengthSeconds.value?: 0), myContext)
            PrefUtil.setTimeRemaining((viewModel.secondsRemaining.value?: 0), myContext)
            //viewModel.setTimerState((viewModel.timerState.value?: TimerState.Stop), myContext)
        }
    }
    private fun initTimer() {
        if (viewModel.getTimerState() == TimerFragment.TimerState.Stop) {
            setNewTimerLength()
        } else {
            setPreviousTimerLength()
        }
        if (viewModel.getTimerState() == TimerFragment.TimerState.Play || viewModel.getTimerState() == TimerFragment.TimerState.Pause) {
            viewModel.secondsRemaining.setValue(getTimeRemaining(myContext))
        } else {
            viewModel.secondsRemaining = viewModel.timerLengthSeconds
        }
    }
    private fun onTimerFinished() {
        //check if timer has ended
        if(viewModel.getTimerState() == TimerFragment.TimerState.Play && viewModel.secondsRemaining.value!! <= 0L){
            Toast.makeText(context, "Time is up!", Toast.LENGTH_SHORT).show()

            // Update the study session count.
            if (viewModel.onStudySession())
                fsdb.onTimerCompletion()
            viewModel.updateSession()
        }
        viewModel.timerState.setValue(TimerState.Stop)
        setNewTimerLength()
        progress_countdown.progress = 0
        PrefUtil.setTimeRemaining((viewModel.timerLengthSeconds.value?: 0), myContext)
        viewModel.secondsRemaining = viewModel.timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    // !!!!
    private fun startTimer() {
        //println((viewModel.secondsRemaining.value?: 0))
        viewModel.timerState.setValue( TimerFragment.TimerState.Play)
        viewModel.timer.setValue(object : CountDownTimer((viewModel.secondsRemaining.value?: 0) * 1000, 1000) {
            override fun onFinish() = onTimerFinished()
            override fun onTick(millisUntilFinished: Long) {
                viewModel.lastCurrentTime.setValue(System.currentTimeMillis())
                viewModel.secondsRemaining.setValue(millisUntilFinished / 1000)
                updateCountdownUI()
            }
        })
        viewModel.timer.value!!.start()
        updateButtons()
    }


    private fun setNewTimerLength() {
        var lengthInMinutes : Long
        if (viewModel.onStudySession())
            lengthInMinutes = PrefUtil.getTimerLength()
        else
            lengthInMinutes = PrefUtil.getBreakTimerLength()
        viewModel.timerLengthSeconds.setValue(lengthInMinutes * 60L)
        //countdown.text = formatTime(lengthInMinutes * 60L)
        //println("SNTL")
        progress_countdown.max = (viewModel.timerLengthSeconds.value!!).toInt()
    }

    private fun setPreviousTimerLength() {
        //println("SPTL")
        viewModel.timerLengthSeconds.setValue(getPrevTimerLength(myContext))
        //progress_countdown.max =  (viewModel.timerLengthSeconds.value!!).toInt()
    }

    private fun updateCountdownUI() {
        title.text = if (viewModel.onStudySession())  "Study Time!" else "Break Time!"
        countdown.text = viewModel.tSecondsRemainingStr()
        progress_countdown.progress = viewModel.secondsRemaining.value!!.toInt()
        //progress_countdown.progress = ( (viewModel.timerLengthSeconds.value!!).toInt() -  (viewModel.secondsRemaining.value!!).toInt())
    }

    private fun updateButtons() {
        //println(viewModel.timerState.value)
        when (viewModel.timerState.value) {
            TimerFragment.TimerState.Play -> {
                play.isEnabled  = false
                pause.isEnabled = true
                stop.isEnabled  = true
            }
            TimerFragment.TimerState.Pause -> {
                play.isEnabled  = true
                pause.isEnabled = false
                stop.isEnabled  = true
            }
            TimerFragment.TimerState.Stop -> {
                play.isEnabled  = true
                pause.isEnabled = false
                stop.isEnabled  = false
            }
            null -> {

            }
        }

    }


}
//--------------------------------------------------------------------//
class PrefUtil {
    companion object {
        private const val PREV_TIMER_ID = "com.hfad.synapflow.timer.prev_timer_id"
        private const val TIMER_STATE_ID = "com.hfad.synapflow.timer.state_id"
        private const val TIME_REMAINING_ID = "com.hfad.synapflow.timer.remaining_id"
        fun getTimerLength(): Long {
            return 25
        }
        fun getBreakTimerLength(): Long {
            return 5
        }

        fun getPrevTimerLength(context: Context): Long {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            return pref.getLong(PREV_TIMER_ID, 0)
        }
        fun setPrevTimerLength(time: Long, context: Context) {
            val pref = PreferenceManager.getDefaultSharedPreferences(context).edit()
            pref.putLong(PREV_TIMER_ID, time).apply()
        }
        fun getTimeRemaining(context: Context): Long {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            return pref.getLong(TIME_REMAINING_ID, 0)
        }
        fun setTimeRemaining(time: Long, context: Context) {
            val pref = PreferenceManager.getDefaultSharedPreferences(context).edit()
            pref.putLong(TIME_REMAINING_ID, time).apply()
        }
    }
}
