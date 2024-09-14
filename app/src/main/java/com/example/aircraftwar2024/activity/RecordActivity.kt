package com.example.aircraftwar2024.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.aircraftwar2024.R
import com.example.aircraftwar2024.ranklist.RankList
import com.example.aircraftwar2024.ranklist.RankListAdapter
import com.example.aircraftwar2024.ranklist.Record
import java.util.Date

class RecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        val listView: ListView = findViewById(R.id.rank_list_listView)
        val textView: TextView = findViewById(R.id.rank_list_textView)
        val button: Button = findViewById(R.id.rank_list_goto_main)
        val gameType = intent.getIntExtra("gameType", 1)
        val score = intent.getIntExtra("score",0)
        var filename   = "easy.json"
        when (gameType) {
            1 -> {
                textView.setTextColor(Color.GREEN)
                textView.text = "简单模式"
                filename =  "easy.json"
            }

            2 -> {
                textView.setTextColor(Color.BLUE)
                textView.text = "普通模式"
                filename =  "medium.json"
            }

            3 -> {
                textView.setTextColor(Color.RED)
                textView.text = "困难模式"
                filename =  "hard.json"
            }
        }
        val rankList = RankList()
        var filePath = filesDir.absolutePath
        filePath += "/$filename"
        rankList.ensureFileExists(filePath)
        val records: MutableList<Record> = rankList.readRecordsFromFile(filePath)
        val rankListAdapter = RankListAdapter(this, R.layout.activity_item, records)
        showInputDialog { newName ->
            records.add(Record(1, newName, score, Date().time))
            rankList.saveRecordsToFile(records,filePath)
            rankListAdapter.updateRank()
            rankListAdapter.notifyDataSetChanged()
        }
        listView.adapter = rankListAdapter
        // 当长按列表项时，删除该项
        listView.setOnItemLongClickListener { parent, _, position, _ ->
            showDeleteDialog { isConfirmed ->
                if (isConfirmed) {
                    // 用户点击了确认
                    val record = parent.adapter as RankListAdapter
                    record.removeAt(position)
                    rankList.saveRecordsToFile(records,filePath)
                    Toast.makeText(this, "删除成功", Toast.LENGTH_LONG).show()
                } else {
                    // 用户点击了取消
                }
            }
            true // 返回true表示消费了事件，不再传递
        }
        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // 可选，取决于你是否想关闭当前的Activity
        }
    }

    private fun showDeleteDialog(onResult: (Boolean) -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("确定删除？")
        builder.setPositiveButton("确认") { _, _ ->
            onResult(true)
        }
        builder.setNegativeButton("取消") { _, _ ->
            onResult(false)
        }
        builder.show()
    }

    // 新增一个用于输入名字的对话框函数
    private fun showInputDialog(onResult: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        // 设置对话框的标题
        builder.setTitle("请输入昵称")
        // 添加一个编辑框用于输入名字
        val input = EditText(this)
        builder.setView(input)
        // 设置确认按钮的点击事件
        builder.setPositiveButton("确认") { _, _ ->
            // 获取输入的名字并回调
            val newName = input.text.toString().trim()
            if (newName.isNotEmpty()) {
                onResult(newName)
            } else {
                Toast.makeText(this, "名字不能为空", Toast.LENGTH_SHORT).show()
            }
        }
        // 设置取消按钮的点击事件
        builder.setNegativeButton("取消", null)
        // 显示对话框
        builder.show()
    }


}