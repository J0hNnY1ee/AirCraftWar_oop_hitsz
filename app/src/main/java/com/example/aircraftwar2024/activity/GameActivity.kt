package com.example.aircraftwar2024.activity

import GameSocketClient
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.aircraftwar2024.R
import com.example.aircraftwar2024.game.BaseGame
import com.example.aircraftwar2024.game.EasyGame
import com.example.aircraftwar2024.game.HardGame
import com.example.aircraftwar2024.game.MediumGame
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GameActivity : AppCompatActivity() {
    private var gameType = 0
    private var otherscore = 0
    var mylooper  =  true
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenHW
        if (intent != null) {
            isOnline = intent.getBooleanExtra("ONLINE_STATE", false)
            gameType = intent.getIntExtra("gameType", 1)
            musicState = intent.getBooleanExtra("MUSIC_STATE", false)
        }
        if(!isOnline) {
            when (gameType) {
                1 -> baseGameView = EasyGame(this)
                2 -> baseGameView = MediumGame(this)
                3 -> baseGameView = HardGame(this)
            }
            setContentView(baseGameView)
            baseGameView?.gameOverFlag?.observe(this, Observer { isGameOver ->
                if (isGameOver) {
                    // 游戏结束时的操作
                    val msg: Message = Message.obtain()
                    msg.what = 0
                    msg.obj = "A"
                    handler.sendMessage(msg)
                }
            })
        }
        else{
            val progressDialog = AlertDialog.Builder(this)
                .setTitle("正在匹配")
                .setMessage("请您耐心等待...")
                .setCancelable(false) // 设置用户不能取消这个对话框
                .create()
            progressDialog.show()

            GlobalScope.launch(Dispatchers.Main) {
                val gameSocketClient = GameSocketClient("10.0.2.2", 9999)
                gameSocketClient.connect()
                gameSocketClient.sendReady()
                gameSocketClient.waitForMatchStart()
                gameType = gameSocketClient.getGameType()!!
                progressDialog.dismiss() // 在主线程中关闭对话框
                when (gameType) {
                    1 -> baseGameView = EasyGame(this@GameActivity)
                    2 -> baseGameView = MediumGame(this@GameActivity)
                    3 -> baseGameView = HardGame(this@GameActivity)
                }
                baseGameView!!.isOnline = true
                setContentView(baseGameView)

                baseGameView?.gameOverFlag?.observe(this@GameActivity, Observer { isGameOver ->
                    if (isGameOver) {
                        // 游戏结束时的操作
                        val msg: Message = Message.obtain()
                        msg.what = 2
                        msg.obj = "C"
                        handler.sendMessage(msg)
                    }
                })
                while (mylooper)
                {
                    delay(500)
                    gameSocketClient.sendScore(baseGameView!!.score)
                    baseGameView?.otherScore = (gameSocketClient.receiveScore() ?: baseGameView?.otherScore)!!
                    otherscore =  baseGameView!!.otherScore
                }
            }
        }
    }
    private  var isConnect = true
    private var baseGameView: BaseGame? = null;
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0 -> showGameOverDialog()
                1 -> {
                    val intent = Intent(this@GameActivity, RecordActivity::class.java)
                    intent.putExtra("gameType", gameType)
                    baseGameView?.let { intent.putExtra("score", it.score) }
                    startActivity(intent)
                    finish()
                }
                2->{
                    setContentView(R.layout.activity_online_game_over)
                    val textView:TextView = findViewById(R.id.onlineScore1)
                    val score = baseGameView?.score
                    textView.text = "您的分数: $score"
                    val textView2:TextView = findViewById(R.id.onlineGameTypeTextview)
                    when(gameType)
                    {
                        1 -> textView2.text = "简单模式"
                        2 -> textView2.text = "普通模式"
                        3 -> textView2.text = "困难模式"
                    }

                    MainScope().launch {
                        while (isConnect) {
                            withContext(Dispatchers.Main) {
                                val textView:TextView = findViewById(R.id.onlineScore2)
                                textView.text = "对手分数: $otherscore"
                            }
                            delay(500) // 每0.5秒更新一次
                        }
                    }
                   val button:Button = findViewById(R.id.onlineGotoMainbutton)
                    button.setOnClickListener{
                        val intent =Intent(this@GameActivity,MainActivity::class.java)
                        mylooper = false
                        isConnect = false
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
    private val screenHW: Unit
        get() {
            //定义DisplayMetrics 对象
            val dm = DisplayMetrics()
            //取得窗口属性
            display!!.getRealMetrics(dm)
            //窗口的宽度
            screenWidth = dm.widthPixels
            //窗口高度
            screenHeight = dm.heightPixels
            Log.i(TAG, "screenWidth : $screenWidth screenHeight : $screenHeight")
        } //    @Override

    companion object {
        private const val TAG = "GameActivity"
        var screenWidth = 0
        var screenHeight = 0
        var musicState = false
        var isOnline: Boolean = false;
    }

    private fun showGameOverDialog() {
        // 创建弹窗
        val builder = AlertDialog.Builder(this)
        builder.setTitle("游戏结束")
        builder.setMessage("您的分数为：" + baseGameView?.score + "\n( ◠‿◠ )")
        builder.setPositiveButton("确认") { _, _ ->
            val msg: Message = Message.obtain()
            msg.what = 1
            msg.obj = "B"
            handler.sendMessage(msg)
        }
        builder.show()
    }
}