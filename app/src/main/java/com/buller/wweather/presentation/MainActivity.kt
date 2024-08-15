package com.buller.wweather.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.Configuration
import androidx.work.WorkManager
import com.buller.wweather.presentation.theme.WWeatherTheme
import com.buller.wweather.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                viewModel.refreshMainWeather()
                viewModel.refreshCities()
            }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            val prefTypeThemeState = viewModel.prefUiState.collectAsStateWithLifecycle()

            if (prefTypeThemeState.value.isAutoUpdate){
                viewModel.fetchWeather(lifecycleOwner = this)
            }

            WWeatherTheme(darkTheme = prefTypeThemeState.value.isTheme) {
                MainScreen(
                    widthSizeClass = widthSizeClass,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}











