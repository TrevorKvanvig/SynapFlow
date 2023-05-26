package com.hfad.synapflow

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.*
import androidx.work.OneTimeWorkRequestBuilder

class MyWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        // Create and show notification
        val notification = NotificationCompat.Builder(applicationContext, "channelId")
            .setContentTitle("SynapFlow")
            .setContentText("YOU BETTER BE STUDYING FOO")
            .setSmallIcon(R.drawable.testingnotify)
            .build()

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)

        // Create a Constraints object that defines when the work can run
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Create a OneTimeWorkRequest for your MyWorkManager class
        val workRequest = OneTimeWorkRequestBuilder<MyWorkManager>()
            .setConstraints(constraints)
            .build()

        // Schedule the work to be executed by the WorkManager
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
        return Result.success()
    }
}