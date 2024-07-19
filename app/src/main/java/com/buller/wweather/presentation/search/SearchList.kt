package com.buller.wweather.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchList(cities: List<String>, modifier: Modifier = Modifier, onItemClick: (String) -> Unit) {
    LazyColumn(modifier = modifier) {
        items(cities) {
            SearchItem(name = it, modifier = modifier, onItemClick)
        }
    }
}

@Composable
fun SearchItem(name: String, modifier: Modifier = Modifier, onItemClick: (String) -> Unit) {
    Box(modifier = modifier
        .clickable {
            onItemClick(name)
        }
        .padding(16.dp).fillMaxWidth()) {
        Text(text = name, fontSize = 18.sp)
    }
}