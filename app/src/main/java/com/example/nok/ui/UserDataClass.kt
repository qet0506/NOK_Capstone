package com.example.nok.ui



data class UserDataClass(
    val username: String,
    val data: UserStats
)

data class UserStats(
    val positive_emotion: Int,
    val negative_emotion: Int,
    val total_problems: Int,
    val total_score: Int,
)