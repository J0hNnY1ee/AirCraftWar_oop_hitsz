package com.example.aircraftwar2024.ranklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.aircraftwar2024.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class RankListAdapter(context: Context, @LayoutRes resource: Int, private var objects: MutableList<Record>) :
    ArrayAdapter<Record>(context, resource, objects) {
        init {
            updateRank()
        }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val record = getItem(position)?:return convertView ?:View(context)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false)
        val recordName = view.findViewById<TextView>(R.id.record_name)
        val recordRank = view.findViewById<TextView>(R.id.record_rank)
        val recordScore = view.findViewById<TextView>(R.id.record_score)
        val recordTime = view.findViewById<TextView>(R.id.record_time)
        recordName.text = record.name
        recordRank.text = record.rank.toString()
        recordScore.text = record.score.toString()
        recordTime.text = formatDate(Date(record.time))
        return view
    }

    private fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        return formatter.format(date)
    }
    fun removeAt(position: Int) {
        objects.removeAt(position)
        updateRank()
        notifyDataSetChanged()
    }
    fun updateRank()
    {
        objects.sortByDescending { it.score }
        var currentRank = 1
        var previousScore: Int? = null
        var previousRank = 1

        // 遍历排序后的记录列表
        for ((index, record) in objects.withIndex()) {
            // 检查当前记录的分数是否与前一个记录的分数相同
            if (previousScore == record.score) {
                // 如果相同，则维持当前排名（即并列）
                record.rank = previousRank
            } else {
                // 如果不同，根据并列的情况决定是否递增排名
                currentRank = if (index == 0 || previousScore != record.score) index + 1 else currentRank + 1
                record.rank = currentRank
                previousRank = currentRank
            }
            // 更新上一个记录的分数和排名，用于下一次比较
            previousScore = record.score
        }
    }
}