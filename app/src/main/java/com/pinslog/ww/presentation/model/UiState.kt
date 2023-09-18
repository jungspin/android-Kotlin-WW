package com.pinslog.ww.presentation.model

/**
* UI 상태를 나타내는 데이터 클래스
* @author jungspin
* @since 2023/09/18 10:38 PM
*/
data class UiState<T> (
    val status: Status = Status.LOADING,
    val message: String = "",
    val data: T? = null,
    val throwable: Throwable? = null,
)

enum class Status {
    LOADING, SUCCESS, FAIL, ERROR
}