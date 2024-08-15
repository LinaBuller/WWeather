package com.buller.wweather.data.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.buller.wweather.di.WorkerModule
import com.buller.wweather.domain.model.City
import com.buller.wweather.domain.repository.RoomRepository
import com.buller.wweather.domain.repository.WeatherRepository
import com.buller.wweather.presentation.home.ExamplePage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoints
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


@HiltWorker
class FetchWeatherWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: WeatherRepository,
    private val roomRepository: RoomRepository
) : CoroutineWorker(appContext, workerParams) {

    constructor(
        appContext: Context,
        workerParams: WorkerParameters
    ) : this(
        appContext,
        workerParams,
        EntryPoints.get(appContext, WorkerModule.WorkerEntryPoint::class.java).weatherRepository(),
        EntryPoints.get(appContext, WorkerModule.WorkerEntryPoint::class.java).roomRepository()
    )

    init {
        Log.d("MyLog", "FetchWeatherWorker initialized")
    }

    override suspend fun doWork(): Result {
        return try {
            Log.d("MyLog", "Starting weather update...")
            val citiesResult =
                suspendCancellableCoroutine{ continuation ->
                    val job = CoroutineScope(Dispatchers.IO).launch {
                        roomRepository.getCities().collect { result ->
                            if (result !is com.buller.wweather.domain.util.Result.Loading) {
                                continuation.resume(result)
                                cancel()
                            } else {
                                Log.d("MyLog", "Cities are still loading...")
                            }
                        }
                    }

                    continuation.invokeOnCancellation {
                        job.cancel()
                    }
                }

            Log.d("MyLog", "Cities fetched: $citiesResult")

            when (citiesResult) {
                is com.buller.wweather.domain.util.Result.Success -> {
                    val cities = citiesResult.data
                    Log.d("MyLog", "Fetched cities: $cities")
                    val results = mutableMapOf<String, ExamplePage>()

                    coroutineScope {
                        val weatherDeferred = cities.map { city ->
                            async {
                                try {
                                    repository.getWeatherData(
                                        q = city.name,
                                        days = 3,
                                        aqi = "no",
                                        alerts = "no"
                                    ).collect { result ->
                                        when (result) {
                                            is com.buller.wweather.domain.util.Result.Loading -> {
                                                Log.d(
                                                    "MyLog",
                                                    "Loading weather data for ${city.name}"
                                                )
                                            }

                                            is com.buller.wweather.domain.util.Result.Success -> {
                                                Log.d(
                                                    "MyLog",
                                                    "Weather data fetched successfully for ${city.name}"
                                                )
                                                results[city.name] = ExamplePage(
                                                    city = city,
                                                    weatherInfo = result.data,
                                                    isLoading = false
                                                )
                                                return@collect
                                            }

                                            is com.buller.wweather.domain.util.Result.Error -> {
                                                Log.e(
                                                    "MyLog",
                                                    "Error fetching weather for ${city.name}: ${result.exception}"
                                                )
                                                results[city.name] = ExamplePage(
                                                    city = city,
                                                    error = result.exception,
                                                    isLoading = false
                                                )
                                                return@collect
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e(
                                        "MyLog",
                                        "Exception while fetching weather for ${city.name}",
                                        e
                                    )
                                    results[city.name] = ExamplePage(
                                        city = city,
                                        error = e,
                                        isLoading = false
                                    )
                                }
                            }
                        }
                        weatherDeferred.awaitAll()
                    }

                    repository.citiesUpdateEvent.emit(results.values.toList())
                    Log.d("MyLog", "Weather update completed.")
                    Result.success()
                }

                is com.buller.wweather.domain.util.Result.Error -> {
                    Log.e("MyLog", "Error fetching cities: ${citiesResult.exception?.message}")
                    val failureData = Data.Builder()
                        .putString(
                            "failure_reason",
                            citiesResult.exception?.message ?: "Unknown error"
                        )
                        .build()
                    Result.failure(failureData)
                }

                is com.buller.wweather.domain.util.Result.Loading -> {
                    Log.d("MyLog", "Cities are still loading.")
                    return Result.retry()
                }
            }
        } catch (e: Exception) {
            val failureData = Data.Builder()
                .putString("failure_reason", e.message ?: "Unknown error")
                .build()
            Log.e("MyLog", "Weather update failure.", e)
            Result.failure(failureData)
        }
    }
}







