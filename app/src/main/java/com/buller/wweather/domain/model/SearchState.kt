package com.buller.wweather.domain.model


sealed interface LocationState {
    val isLoading: Boolean
    val errorMessages: Throwable?

    data class SearchNoInfo(
        override val isLoading: Boolean,
        override val errorMessages: Throwable?
    ) : LocationState

    data class SearchHasInfo(
        val locationList: List<LocationInfo>,
        override val isLoading: Boolean,
        override val errorMessages: Throwable?
    ) : LocationState
}

data class SearchState(
    val locationList: List<LocationInfo>?,
    val isLoading: Boolean = false,
    val error: Throwable? = null
) {
    fun toUiState(): LocationState = if (locationList == null) {
        LocationState.SearchNoInfo(isLoading = isLoading, errorMessages = error)
    }else{
        LocationState.SearchHasInfo(
            locationList = locationList,
            isLoading = isLoading,
            errorMessages = error
        )
    }
}