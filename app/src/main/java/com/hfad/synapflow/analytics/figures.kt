package com.hfad.synapflow.analytics

import android.util.Log
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.hfad.synapflow.FirestoreData
import com.hfad.synapflow.fsdb
import java.time.LocalDate


class Figures {
    //private lateinit var fsdb : FirestoreData
    private lateinit var dateData : MutableMap<String, Any>
    private lateinit var lineData: LineData
    private lateinit var barData: BarData
    private lateinit var dateList: MutableList<String>
    private lateinit var barRowList: MutableList<String>
    constructor() {
        //fsdb = fsdata
        dateData = mutableMapOf<String, Any>()
        dateList = mutableListOf<String>()
        barRowList = mutableListOf<String>()
        //refreshData()
        //drawLineData()
        //drawBarData()
        println("Idk")
    }

    public fun refreshData() {
        dateData = fsdb.getCompletionDates()
        println(dateData)
    }

    fun getDayCounts(date : String) : Float {
        val data = dateData?.get(date)
        if (data is List<*>) {
            println(date)
            println(data.filterIsInstance<String>().size.toFloat())
            return data.filterIsInstance<String>().size.toFloat()
        }
        return 0f
    }


    private fun getWeekLine(weekAgo: Int = 0) : MutableList<Entry> {

        refreshData()
        val test = mutableListOf<Entry>()
        dateList = mutableListOf<String>()
        val today = LocalDate.now()
        for (i in 0 .. 7) {
            val date = today.minusDays(i.toLong())
            dateList.add("${date.monthValue}-${date.dayOfMonth}")
            test.add(Entry(i.toFloat(), getDayCounts(date.toString())))
        }
        return test
    }
    public fun linePlot(frag: LineChart) {

        refreshData()
        // Needs to be the line chart fragment passed in
        val mpLineChart =  frag
        drawLineData()
        mpLineChart.setData(lineData)

        mpLineChart.getXAxis().setValueFormatter(IndexAxisValueFormatter(dateList))

        mpLineChart.getDescription().setEnabled(false)
        mpLineChart.invalidate()
    }
    private fun drawLineData() {
        // Get the line (x,y) points.
        val test = getWeekLine()
        // Now the weird part add to a list, to another list to another..
        var lineDs = LineDataSet(test, "Daily Study Counts")
        lineDs.setCircleColor(0xFFF7D86D.toInt())
        lineDs.setColors(mutableListOf<Int>(0xFF47C6B9.toInt()))
        var dataSet = mutableListOf<ILineDataSet>()

        dataSet.add(lineDs)
        lineData = LineData(dataSet)
    }
    private fun getBarCount() : MutableList<BarEntry>{

        var weekIncr = 4
        barRowList = mutableListOf<String>()
        val test = mutableListOf<BarEntry>()
        val today = LocalDate.now()
        for (j in 4 downTo 1) {
            var counter: Float = 0f
            for (i in 7 downTo 1) {
                val date = today.minusDays(((j * 7) - i).toLong()).toString()
                counter += getDayCounts(date)
            }
            weekIncr--
            val date =  today.minusDays(((j * 7) - 1).toLong())
            test.add(BarEntry(j.toFloat(), counter))
            barRowList.add("Wk of ${date.monthValue}-${date.dayOfMonth}")
        }
        return test
    }

    private fun drawBarData() {
        val barList = getBarCount()

        var barDs = BarDataSet(barList, "Last Months Study Sessions by Week")
        barDs.setColors(mutableListOf<Int>(0xFF4C4C4C.toInt(), 0xFFF7D86D.toInt(),0xFF9BD5A9.toInt(), 0xFF47C6B9.toInt()))
        var barDataSet = mutableListOf<IBarDataSet>() // B G Y GR

        barDataSet.add(barDs)
        barData = BarData(barDataSet)
    }


    public fun barPlot(frag: BarChart) {

        refreshData()
        val mpBarChart = frag
        drawBarData()
        mpBarChart.setData(barData)
        print(barRowList)
        //mpBarChart.getXAxis().setValueFormatter(IndexAxisValueFormatter(barRowList))
        mpBarChart.xAxis.setEnabled(false)
        //mpBarChart.getDescription().setEnabled(false)
        val xAxis = mpBarChart.getXAxis()
        mpBarChart.invalidate()

    }
}
