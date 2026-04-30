package com.bernat.shelfie.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey val id: Int = 1,
    val lastLoginDate: String,
    val totalBooksAdded: Int = 0
)
