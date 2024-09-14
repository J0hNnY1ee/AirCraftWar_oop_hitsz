package com.example.aircraftwar2024.ranklist

import com.example.aircraftwar2024.activity.RecordActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

class RankList : RecordDao {
    override fun saveRecordsToFile(records: MutableList<Record>, filePath: String) {
        val jsonStr = Json.encodeToString(records)
        File(filePath).writeText(jsonStr)
    }

    override fun readRecordsFromFile(filePath: String): MutableList<Record> {
        val file = File(filePath)
        val jsonString =
            if (file.exists() && file.length() > 0) file.readText(Charsets.UTF_8) else "[]"
        return Json.decodeFromString(jsonString)
    }

    fun ensureFileExists(filePath: String) {
        val file = File(filePath)

        // 检查文件是否存在，如果不存在则创建
        if (!file.exists()) {
            try {
                // 使用true作为第二个参数，表示如果父目录不存在也一并创建
                file.createNewFile()
                println("文件已创建: $filePath")
            } catch (e: IOException) {
                println("文件创建失败: ${e.message}")
            }
        } else {
            println("文件已存在: $filePath")
        }
    }
}