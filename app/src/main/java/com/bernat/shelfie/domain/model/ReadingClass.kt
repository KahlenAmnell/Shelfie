package com.bernat.shelfie.domain.model

enum class ReadingStatus {
    WANT_TO_READ,
    READING,
    READ
}

data class ReadingClass(
    val status: ReadingStatus = ReadingStatus.WANT_TO_READ,
    val currentPage: Int = 0,
    val startDate: Long? = null,
    val finishDate: Long? = null
)
