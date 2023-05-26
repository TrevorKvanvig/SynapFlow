package com.hfad.synapflow

import android.view.View
import androidx.annotation.IdRes
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hfad.synapflow.utils.TestDayViewContainer
import com.hfad.synapflow.utils.getView
import com.hfad.synapflow.utils.openCalendarFragment
import com.hfad.synapflow.utils.runOnMain
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.YearMonth


@RunWith(AndroidJUnit4::class)
class CalendarTests {
    @get:Rule
    val homeScreenRule = ActivityScenarioRule(HomeActivity::class.java)

    private val currentMonth = YearMonth.now()

    @Test
    fun dayBinderIsCalledOnDayChanged() {
        openCalendarFragment()

        val calendarView = getView<CalendarView>(R.id.calendarView)

        var boundDay: CalendarDay? = null

        val changedDate = currentMonth.atDay(4)

        runOnMain {
            calendarView.dayBinder = object : MonthDayBinder<TestDayViewContainer> {
                override fun create(view: View) = TestDayViewContainer(view)
                override fun bind(container: TestDayViewContainer, data: CalendarDay) {
                    boundDay = data
                }
            }
        }

        // Allow the calendar to be rebuilt due to dayBinder change.
        Thread.sleep(2000)

        runOnMain {
            calendarView.notifyDateChanged(changedDate)
        }

        // Allow time for date change event to be propagated.
        Thread.sleep(2000)

        Assert.assertEquals(changedDate, boundDay?.date)
        Assert.assertEquals(DayPosition.MonthDate, boundDay?.position)
    }

    @Test
    fun programmaticScrollWorksAsExpected() {
        openCalendarFragment()

        val calendarView = getView<CalendarView>(R.id.calendarView)

        Assert.assertNotNull(calendarView.findViewWithTag(currentMonth.atDay(1).hashCode()))

        val nextFourMonths = currentMonth.plusMonths(4)

        runOnMain {
            calendarView.scrollToMonth(nextFourMonths)
        }

        Thread.sleep(2000)

        Assert.assertNull(calendarView.findViewWithTag(currentMonth.atDay(1).hashCode()))
        Assert.assertNotNull(calendarView.findViewWithTag(nextFourMonths.atDay(1).hashCode()))
    }

    private fun <T : View> getView(@IdRes id: Int): T = homeScreenRule.getView(id)
}