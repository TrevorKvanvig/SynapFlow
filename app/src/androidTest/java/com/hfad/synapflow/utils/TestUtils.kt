package com.hfad.synapflow.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import java.lang.Thread.sleep
import com.hfad.synapflow.R

internal fun runOnMain(action: () -> Unit) {
    InstrumentationRegistry.getInstrumentation().runOnMainSync(action)
}

internal fun openCalendarFragment() {
    onView(withId(R.id.nav_calendar)).perform(ViewActions.click())
}

internal fun <T : View> ActivityScenarioRule<*>.getView(@IdRes id: Int): T {
    lateinit var view: T
    this.scenario.onActivity { activity ->
        view = activity.findViewById(id)
    }
    sleep(1000)
    return view
}
