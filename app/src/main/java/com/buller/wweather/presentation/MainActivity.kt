package com.buller.wweather.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buller.wweather.R
import com.buller.wweather.presentation.theme.WWeatherTheme
import com.buller.wweather.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private val permissionToRequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>
    private var permissionDeniedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel = hiltViewModel<HomeViewModel>()

            val prefTypeThemeState = viewModel.prefUiState.collectAsStateWithLifecycle()
            if (prefTypeThemeState.value.isAutoUpdate) {
                viewModel.fetchWeather(lifecycleOwner = this)
            }

            permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { perms ->
                val allPermissionsGranted = perms.values.all { it }
                if (allPermissionsGranted) {
                    permissionDeniedCount = 0
                    viewModel.refreshMainWeather()
                } else {
                    permissionDeniedCount++
                    Log.d("MyLog", "Permission denied $permissionDeniedCount times")
                    if (permissionDeniedCount >= 1) {
                        mainViewModel.showPermissionDialog()
                    }
                }
            }

            val appSettingsLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                Log.d("MyLog", "Result received: $result")
                checkPermissionsAndRefresh(viewModel)
            }

            LaunchedEffect(Unit) {
                checkPermissionsAndRefresh(viewModel = viewModel)
            }

            LaunchedEffect(key1 = Unit) {
                permissionLauncher.launch(permissionToRequest)
            }


            val showDialog = mainViewModel.showDialog.collectAsStateWithLifecycle()

            if (showDialog.value) {
                PermissionDialog(
                    onDismiss = { mainViewModel.hidePermissionDialog() },

                    onOkClick = {
                        mainViewModel.hidePermissionDialog()
                        openAppSettings(appSettingsLauncher)
                    }
                )
            }

            WWeatherTheme(darkTheme = prefTypeThemeState.value.isTheme) {
                MainScreen()
            }
        }
    }

    private fun checkPermissionsAndRefresh(viewModel: HomeViewModel) {
        val notGrantedPermissions = permissionToRequest.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGrantedPermissions.isEmpty()) {
            viewModel.refreshMainWeather()
        } else {
            permissionLauncher.launch(notGrantedPermissions.toTypedArray())
        }
    }

    private fun openAppSettings(
        appSettingsLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
    ) {
        Log.d("MyLog", "Opening app settings")
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        appSettingsLauncher.launch(intent)
    }


}

