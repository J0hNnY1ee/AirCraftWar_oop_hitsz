package com.example.aircraftwar2024.ranklist



import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Record(
    var rank: Int,
    val name: String,
    var score: Int,
    val time: Long
)
