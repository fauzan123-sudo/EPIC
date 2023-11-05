package com.example.epic.data.model.statistic

data class StatisticResponse(
    val `data`: List<Int>,
    val message: String,
    val status: Boolean
)