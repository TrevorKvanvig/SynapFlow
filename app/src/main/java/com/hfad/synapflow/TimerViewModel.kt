package com.hfad.synapflow

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firestore.v1.StructuredAggregationQuery.Aggregation.Count
import com.hfad.synapflow.TimerFragment
import java.time.LocalDate


class TimerViewModel :ViewModel() { // (application: Application):AndroidViewModel(application) {

    var studyTimerMax = MutableLiveData<Long>(PrefUtil.getTimerLength() * 60)
    var breakTimerMax = MutableLiveData<Long>(PrefUtil.getBreakTimerLength() * 60)
    var timerLengthSeconds = MutableLiveData<Long>(0)
    var timerState = MutableLiveData<TimerFragment.TimerState>(TimerFragment.TimerState.Stop)
    var secondsRemaining = MutableLiveData<Long>(studyTimerMax.value!!)
    var secondsRemainingStr = MutableLiveData<String>()
    var studySessionState = MutableLiveData<Boolean>(true)
    var lastCurrentTime = MutableLiveData<Long>()
    var timer = MutableLiveData<CountDownTimer>()


    fun updateSession() {
        if (studySessionState.value!!)
            studySessionState.setValue(false)
        else
            studySessionState.setValue(true)
    }

    fun onStudySession() : Boolean {
        if (studySessionState.value?: true)
            return true
        return false
    }

    fun tSecondsRemainingStr() : String? {
        val minutesUntilFinished = (secondsRemaining.value?: 0) / 60
        val secondsInMinutesUntilFinished = (secondsRemaining.value?: 0) - minutesUntilFinished * 60
        val secondsStr = secondsInMinutesUntilFinished.toString()
        var newStr = "$minutesUntilFinished:${
            if (secondsStr.length == 2) secondsStr
            else "0"+ secondsStr}"
        secondsRemainingStr.setValue(newStr)
        return secondsRemainingStr.value?.toString()
    }

    fun getTimerState(): TimerFragment.TimerState? {
        return timerState.value
    }
}