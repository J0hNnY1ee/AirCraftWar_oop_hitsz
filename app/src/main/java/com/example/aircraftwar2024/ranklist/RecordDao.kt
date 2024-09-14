package com.example.aircraftwar2024.ranklist

interface RecordDao {
    fun saveRecordsToFile(records: MutableList<Record>, filePath: String)
    fun readRecordsFromFile(filePath: String):MutableList<Record>
}