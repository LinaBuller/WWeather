package com.buller.wweather.domain.model


sealed interface PageState {
    val isLoading: Boolean
    val errorMessages: Throwable?

    data class NoInfo(
        override val isLoading: Boolean,
        override val errorMessages: Throwable?
    ) : PageState

    data class HasInfo(
        val pagerList: List<ExamplePage>,
        override val isLoading: Boolean,
        override val errorMessages: Throwable?
    ) : PageState
}

data class PagerState(
    val pagerList: List<ExamplePage>?,
    val isLoading: Boolean = true,
    val error: Throwable? = null
){
    fun toUiState(): PageState =
        if (pagerList == null) {
            PageState.NoInfo(
                isLoading = isLoading,
                errorMessages = error
            )
        } else {
            PageState.HasInfo(
                pagerList = pagerList,
                isLoading = isLoading,
                errorMessages = error
            )
        }
}