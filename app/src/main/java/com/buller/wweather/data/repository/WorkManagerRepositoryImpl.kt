package com.buller.wweather.data.repository

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.buller.wweather.data.workers.FetchWeatherWorker
import com.buller.wweather.domain.repository.WorkManagerRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerRepositoryImpl @Inject constructor(
    @ApplicationContext
    val context: Context
) : WorkManagerRepository {

    private val workManager = WorkManager.getInstance(context)

    override fun fetchWeather(lifecycleOwner: LifecycleOwner) {
        val fetchWeatherRequest = PeriodicWorkRequestBuilder<FetchWeatherWorker>(
            60, TimeUnit.MINUTES
        ).build()

        workManager.enqueueUniquePeriodicWork(
            "fetchWeather",
            ExistingPeriodicWorkPolicy.KEEP,
            fetchWeatherRequest
        )

        workManager.getWorkInfoByIdLiveData(fetchWeatherRequest.id)
            .observe(lifecycleOwner) { workInfo ->
                if (workInfo != null) {
                    Log.d("MyLog", "Current state: ${workInfo.state}, output: ${workInfo.outputData}")
                    when (workInfo.state) {
                        WorkInfo.State.ENQUEUED -> {
                            Log.d("MyLog", "WorkRequest scheduled successfully")
                        }
                        WorkInfo.State.SUCCEEDED -> {
                            Log.d("MyLog", "WorkRequest succeeded")
                        }
                        WorkInfo.State.FAILED -> {
                            val failureReason = workInfo.outputData.getString("failure_reason")
                            Log.e("MyLog", "WorkRequest failed: $failureReason")
                        }
                        else -> {
                            Log.d("MyLog", "WorkRequest state: ${workInfo.state}")
                        }
                    }
                }
            }
    }
}