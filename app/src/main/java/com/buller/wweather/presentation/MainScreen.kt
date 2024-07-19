package com.buller.wweather.presentation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.buller.wweather.presentation.home.HomeViewModel



@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    widthSizeClass: WindowWidthSizeClass,
    viewModel: HomeViewModel
) {
    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

    NavGraph(
        isExpandedScreen = isExpandedScreen,
        viewModel = viewModel
    )
}

