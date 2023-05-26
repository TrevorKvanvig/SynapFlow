package com.hfad.synapflow

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.Timestamp
import com.hfad.synapflow.Classes.NotificationBroadcastReceiver
import java.text.SimpleDateFormat
import java.util.*


class AddTaskFragment : Fragment() {

    // Used for dates and times
    var startYear: Int = 0
    var startMonth: Int = 0
    var startDay: Int = 0
    var startHour: Int = 0
    var startMinute: Int = 0
    var endYear: Int = 0
    var endMonth: Int = 0
    var endDay: Int = 0
    var endHour: Int = 0
    var endMinute: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Add New Task | Synapflow"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the spinner for the task's category
        val spinner = view.findViewById<Spinner>(R.id.category_spinner)
        val options = resources.getStringArray(R.array.taskCategoryOptions)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Set up the date and time picker dialogs for the start date and time EditTexts
        setStartDateTimeListener(view)

        // Set up the SeekBar for the priority
        val prioritySeekBar = view.findViewById<SeekBar>(R.id.priority_seekbar)
        val priorityLabel = view.findViewById<TextView>(R.id.priorityLabel)

        // Set up the text labels for each SeekBar position
        val priorityLabels = listOf("Very Low", "Low", "Medium", "High", "ASAP")

        // Listen for changes to the SeekBar position
        prioritySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update the text label to reflect the current SeekBar position
                priorityLabel.text = priorityLabels[progress]
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        view.findViewById<Button>(R.id.addTaskButton).setOnClickListener {
            // Get the task name
            val taskName = view.findViewById<EditText>(R.id.name_txt).text.toString()
            val taskDescription = view.findViewById<EditText>(R.id.description_txt).text.toString()
            // Get the task category
            val taskCategory = spinner.selectedItem.toString()
            // Get the task priority
            val taskPriority = prioritySeekBar.progress.toLong()
            val startDateTime = Date(startYear - 1900, startMonth, startDay, startHour, startMinute)

            val startDateStamp : Timestamp;
            val endDateStamp : Timestamp;

            if(startYear > 1900) {
                startDateStamp =  Timestamp(startDateTime);
            } else {
                startDateStamp = Timestamp(Date())
            }

            // Create a new task object
            val task = Task(taskName, taskDescription, taskCategory, startDateStamp, taskPriority)

            // Add the task to the database
            FirestoreData().addTask(task)

            val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val notificationIntent = Intent(context, NotificationBroadcastReceiver::class.java)
            notificationIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, startYear)
            calendar.set(Calendar.MONTH, startMonth)
            calendar.set(Calendar.DAY_OF_MONTH, startDay)
            calendar.set(Calendar.HOUR_OF_DAY, startHour)
            calendar.set(Calendar.MINUTE, startMinute)
            calendar.set(Calendar.SECOND, 0)

            Log.d("calendar", calendar.time.toString())

            // Set the alarm to trigger at the specified date and time
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

            // Exit fragment
            requireActivity().supportFragmentManager.popBackStackImmediate()
            fsdb.getTaskMap()
        }
    }

    /**
     * Set up the date and time picker dialogs for the start date and time EditTexts
     */
    private fun setStartDateTimeListener(view: View) {
        val dateEditText = view.findViewById<EditText>(R.id.startDateEditText)
        val timeEditText = view.findViewById<EditText>(R.id.startTimeEditText)
        val calendar = Calendar.getInstance()
        // Set up the date picker dialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                startYear = year
                startMonth = month
                startDay = dayOfMonth
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                dateEditText.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Set up the time picker dialog
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                startHour = hourOfDay
                startMinute = minute
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                timeEditText.setText(timeFormat.format(calendar.time))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        // Set the date picker dialog to open when the date EditText is clicked
        dateEditText.setOnClickListener {
            datePickerDialog.show()
        }
        // Set the time picker dialog to open when the time EditText is clicked
        timeEditText.setOnClickListener {
            timePickerDialog.show()
        }
    }


}