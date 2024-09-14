package com.example.aircraftwar2024.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.MutableLiveData
import com.example.aircraftwar2024.ImageManager
import com.example.aircraftwar2024.R
import com.example.aircraftwar2024.activity.GameActivity
import com.example.aircraftwar2024.aircraft.AbstractEnemyAircraft
import com.example.aircraftwar2024.aircraft.BossEnemy
import com.example.aircraftwar2024.aircraft.HeroAircraft
import com.example.aircraftwar2024.basic.AbstractFlyingObject
import com.example.aircraftwar2024.bullet.AbstractBullet
import com.example.aircraftwar2024.factory.enemy_factory.BossFactory
import com.example.aircraftwar2024.factory.enemy_factory.EliteFactory
import com.example.aircraftwar2024.factory.enemy_factory.EnemyFactory
import com.example.aircraftwar2024.factory.enemy_factory.MobFactory
import com.example.aircraftwar2024.supply.AbstractFlyingSupply
import java.util.LinkedList
import java.util.Random
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 游戏逻辑抽象基类，遵循模板模式，action() 为模板方法
 * 包括：游戏主面板绘制逻辑，游戏执行逻辑。
 * 子类需实现抽象方法，实现相应逻辑
 * @author hitsz
 */

abstract class BaseGame(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {
    private val bgmP: MediaPlayer = MediaPlayer.create(context, R.raw.bgm)
    private val bossBgmP: MediaPlayer = MediaPlayer.create(context, R.raw.bgm_boss)
    private var mbLoop = true//控制绘画线程的标志位
    private val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
    private val mysp: SoundPool =
        SoundPool.Builder().setMaxStreams(10).setAudioAttributes(audioAttributes).build()
    private val soundPoolMap = HashMap<Int, Int>()
    private val mSurfaceHolder: SurfaceHolder = this.holder
    private var canvas: Canvas? = null //绘图的画布
    private val mPaint: Paint = Paint()
    //点击屏幕位置
    private var clickX = 0f
    private var clickY = 0f
    private var backGroundTop = 0
    /**
     * 背景图片缓存，可随难度改变
     */
    protected var backGround: Bitmap? = null

    /**
     * 周期（ms)
     * 控制英雄机射击周期，默认值设为简单模式
     */
    protected var heroShootCycle = 10.0
    private var heroShootCounter = 0
    protected var tickCycle = Int.MAX_VALUE
    protected var tickCounter = 0

    /**
     * 周期（ms)
     * 控制敌机射击周期，默认值设为简单模式
     */
    protected var enemyShootCycle = 20.0
    private var enemyShootCounter = 0

    /**
     * 游戏难度，敌机血量和速度的提升倍率，
     * 普通和困难模式随着时间增加而提高难度，提升普通和精英敌机的速度和血量
     */
    protected var gameLevel = 1.0

    /**
     * 普通和困难模式中
     * 当得分每超过一次bossScoreThreshold，则产生一次boss机
     */
    protected var bossScoreThreshold = Int.MAX_VALUE

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private val timeInterval = 16
    private val enemyAircrafts: MutableList<AbstractEnemyAircraft?>
    private val flyingSupplies: MutableList<AbstractFlyingSupply?>
    private val heroBullets: MutableList<AbstractBullet?>
    private val enemyBullets: MutableList<AbstractBullet?>
    protected var enemyMaxNumber = 5

    //    var gameOverFlag = false
    val gameOverFlag = MutableLiveData(false)
    var score = 0
    var otherScore = 0
    var isOnline = false
    /**
     * 周期（ms)
     * 控制英雄机射击周期，默认值设为简单模式
     */
    private val cycleDuration = 60
    private var cycleTime = 0
    protected var bossLevel = 1.0


    /**
     * 产生小敌机和精英敌机。
     * 返回产生的敌机，可以为空链表（即表示不产生）；不能返回null！
     */
    private var enemyCounter = 0
    protected var enemyCycle = 20.0

    /**
     * 控制精英机产生概率，默认值设为简单模式
     */
    protected var eliteProb = 0.3

    /**
     * 敌机工厂
     */
    private val mobEnemyFactory: EnemyFactory
    private val eliteEnemyFactory: EnemyFactory
    private val bossEnemyFactory: EnemyFactory
    private val random = Random()

    init {
        //设置画笔
        mSurfaceHolder.addCallback(this)
        this.isFocusable = true
        ImageManager.initImage(context)

        // 初始化英雄机
        HeroAircraft.reset()
        enemyAircrafts = CopyOnWriteArrayList()
        heroBullets = CopyOnWriteArrayList()
        enemyBullets = CopyOnWriteArrayList()
        flyingSupplies = CopyOnWriteArrayList()
        mobEnemyFactory = MobFactory()
        eliteEnemyFactory = EliteFactory()
        bossEnemyFactory = BossFactory()
        heroController()
        soundPoolMap[1] = mysp.load(context, R.raw.game_over, 1)
        soundPoolMap[2] = mysp.load(context, R.raw.bullet_hit, 1)
        soundPoolMap[3] = mysp.load(context, R.raw.get_supply, 1)
        soundPoolMap[4] = mysp.load(context, R.raw.bomb_explosion, 1)
    }

    private fun heroShootAction() {
        // 英雄射击
        heroBullets.addAll(HeroAircraft.shoot()!!)
    }

    private fun enemyShootAction() {
        // 敌机射击
        for (enemyAircraft in enemyAircrafts) {
            enemyBullets.addAll(enemyAircraft!!.shoot()!!)
        }
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    private fun action() {

        //new Thread(new Runnable() {
        val task = Runnable {
            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {

                // produceBoss 根据游戏难度策略产生 BOSS
                //根据游戏设定，随着时间提高游戏难度
                tick()
                enemyAircrafts.addAll(produceBoss()!!)

                // produceEnemy 根据游戏难度策略产生精英敌机和小敌机
                enemyAircrafts.addAll(produceEnemy())
                if (shouldHeroShoot()) {
                    heroShootAction()
                }
                if (shouldEnemyShoot()) {
                    enemyShootAction()
                }
            }
            // 子弹移动
            bulletsMoveAction()
            // 飞机移动
            aircraftsMoveAction()
            suppliesMoveAction()

            // 撞击检测
            try {
                crashCheckAction()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            musicControl()
            // 后处理
            postProcessAction()

        }
        task.run()
    }

    /**
     * 每个时刻均调用一次。
     * 普通和困难模式随着时间增加会提高游戏难度
     */
    protected abstract fun tick()
    private fun suppliesMoveAction() {
        for (flyingSupply in flyingSupplies) {
            flyingSupply!!.forward()
        }
    }

    protected open fun produceBoss(): List<AbstractEnemyAircraft?>? {
        val res: MutableList<AbstractEnemyAircraft?> = LinkedList()

        //当得分每超过一次bossScoreThreshold，且当前无boos机存在，则产生一次boss机
        // 普通模式boss机的血量不会变化
        if (score >= bossScoreThreshold && !existBoss()) {
            bossScoreThreshold += bossScoreThreshold
            res.add(bossEnemyFactory.createEnemyAircraft(bossLevel))
        }
        return res
    }

    private fun existBoss(): Boolean {
        for (enemyAircraft in enemyAircrafts) {
            if (enemyAircraft is BossEnemy) {
                return true
            }
        }
        return false
    }

    private fun produceEnemy(): List<AbstractEnemyAircraft?> {
        enemyCounter++
        if (enemyCounter >= enemyCycle) {
            // 每当计数次数达到周期，重置计数器，执行相关指令
            // 后射击周期同理
            enemyCounter = 0
            val res: MutableList<AbstractEnemyAircraft?> = LinkedList()

            // 产生敌机
            if (enemyAircrafts.size < enemyMaxNumber) {
                if (random.nextDouble() < eliteProb) {
                    //精英机
                    res.add(eliteEnemyFactory.createEnemyAircraft(gameLevel))
                } else {
                    //普通敌机
                    res.add(mobEnemyFactory.createEnemyAircraft(gameLevel))
                }
            }
            return res
        }
        return LinkedList()
    }

    private fun shouldHeroShoot(): Boolean {
        heroShootCounter++
        if (heroShootCounter >= heroShootCycle) {
            heroShootCounter = 0
            return true
        }
        return false
    }

    private fun shouldEnemyShoot(): Boolean {
        enemyShootCounter++
        if (enemyShootCounter >= enemyShootCycle) {
            enemyShootCounter = 0
            return true
        }
        return false
    }

    private fun heroController() {
        setOnTouchListener { view, motionEvent ->
            clickX = motionEvent.x
            clickY = motionEvent.y
            HeroAircraft.setLocation(clickX.toDouble(), clickY.toDouble())
            when (motionEvent.action) {
                MotionEvent.ACTION_UP -> {
                    // 如果点击在有效区域内，则先调用performClick
                    if ( clickX in 0.0.. GameActivity.screenWidth.toDouble() && clickY in 0.0..GameActivity.screenHeight.toDouble()) {
                        view.performClick()
                    }
                }
            }
            // 根据是否在此处理事件返回true或false，false会让事件继续传递给其他监听器
            motionEvent.action != MotionEvent.ACTION_UP
        }
    }

    private fun timeCountAndNewCycleJudge(): Boolean {
        cycleTime += timeInterval
        // 只需检查cycleTime是否达到或超过cycleDuration即可决定是否进入新周期
        return if (cycleTime >= cycleDuration) {
            // 进入新周期，对cycleTime进行取余操作重置周期计数
            cycleTime %= cycleDuration
            true
        } else {
            false
        }
    }

    private fun bulletsMoveAction() {
        for (bullet in heroBullets) {
            bullet!!.forward()
        }
        for (bullet in enemyBullets) {
            bullet!!.forward()
        }
    }

    private fun aircraftsMoveAction() {
        for (enemyAircraft in enemyAircrafts) {
            enemyAircraft!!.forward()
        }
    }

    /**
     * 碰撞检测：
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    @Throws(InterruptedException::class)
    private fun crashCheckAction() {
        // 敌机子弹攻击英雄
        for (bullet in enemyBullets) {
            if (bullet!!.notValid()) {
                continue
            }
            if (HeroAircraft.crash(bullet)) {
                HeroAircraft.decreaseHp(bullet.power)
                bullet.vanish()
            }
        }

        // 英雄子弹攻击敌机
        for (bullet in heroBullets) {
            if (bullet!!.notValid()) {
                continue
            }
            for (enemyAircraft in enemyAircrafts) {
                if (enemyAircraft!!.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    soundPoolMap[2]?.let {
                        if (GameActivity.musicState) mysp.play(
                            it, 1F, 1F, 0, 0, 1F
                        )
                    }
                    enemyAircraft.decreaseHp(bullet.power)
                    bullet.vanish()
                    if (enemyAircraft.notValid()) {
                        //获得分数，产生道具补给
                        score += enemyAircraft.score()
                        flyingSupplies.addAll(enemyAircraft.generateSupplies())
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(HeroAircraft) || HeroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish()
                    HeroAircraft.decreaseHp(Int.MAX_VALUE)
                }
            }
        }

        // 我方获得补给
        for (flyingSupply in flyingSupplies) {
            if (flyingSupply!!.notValid()) {
                continue
            }
            if (HeroAircraft.crash(flyingSupply) || flyingSupply.crash(HeroAircraft)) {
                soundPoolMap[3]?.let {
                    if (GameActivity.musicState) mysp.play(
                        it, 1F, 1F, 0, 0, 1F
                    )
                }
                flyingSupply.activate()
                flyingSupply.vanish()
            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     *
     *
     * 无效的原因可能是撞击或者飞出边界
     */
    private fun postProcessAction() {
        enemyBullets.removeIf { obj: AbstractBullet? -> obj!!.notValid() }
        heroBullets.removeIf { obj: AbstractBullet? -> obj!!.notValid() }
        enemyAircrafts.removeIf { obj: AbstractEnemyAircraft? -> obj!!.notValid() }
        flyingSupplies.removeIf { obj: AbstractFlyingSupply? -> obj!!.notValid() }
        if (HeroAircraft.notValid()) {
            gameOverFlag.postValue(true)
            soundPoolMap[1]?.let { if (GameActivity.musicState) mysp.play(it, 1F, 1F, 0, 0, 1F) }
            if (bgmP.isPlaying)
                bgmP.stop()
            if (bossBgmP.isPlaying)
                bossBgmP.stop()
            mbLoop = false
            Log.i(TAG, "HeroAircraft is not Valid")
            return
        }
    }

    private fun draw() {
        canvas = mSurfaceHolder.lockCanvas()
        if (canvas == null) {
            return
        }

        //绘制背景，图片滚动
        canvas!!.drawBitmap(
            backGround!!, 0f, (backGroundTop - backGround!!.getHeight()).toFloat(), mPaint
        )
        canvas!!.drawBitmap(backGround!!, 0f, backGroundTop.toFloat(), mPaint)
        backGroundTop += 1
        if (backGroundTop == GameActivity.screenHeight) backGroundTop = 0

        //先绘制子弹，后绘制飞机
        paintImageWithPositionRevised(enemyBullets) //敌机子弹
        paintImageWithPositionRevised(heroBullets) //英雄机子弹
        paintImageWithPositionRevised(enemyAircrafts) //敌机
        paintImageWithPositionRevised(flyingSupplies) //道具
        ImageManager.HERO_IMAGE?.let {
            canvas!!.drawBitmap(
                it,
                (HeroAircraft.locationX - ImageManager.HERO_IMAGE!!.getWidth() / 2).toFloat(),
                (HeroAircraft.locationY - ImageManager.HERO_IMAGE!!.getHeight() / 2).toFloat(),
                mPaint
            )
        }

        //画生命值
        paintScoreAndLife()
        if(isOnline)
            paintOnlineScore()
        mSurfaceHolder.unlockCanvasAndPost(canvas)
    }

    private fun paintImageWithPositionRevised(objects: List<AbstractFlyingObject?>) {
        if (objects.isEmpty()) {
            return
        }
        for (`object` in objects) {
            val image = `object`!!.image ?: error(objects.javaClass.getName() + " has no image! ")
            canvas?.drawBitmap(
                image,
                (`object`.locationX - image.getWidth() / 2).toFloat(),
                (`object`.locationY - image.getHeight() / 2).toFloat(),
                mPaint
            )
        }
    }

    private fun paintScoreAndLife() {
        /**TODO:动态绘制文本框显示英雄机的分数和生命值 */
        // 设置画笔属性，如颜色、字体大小、样式等
        mPaint.setColor(Color.RED) // 文字颜色为白色
        mPaint.textSize = 50f // 文字大小，根据实际情况调整
        mPaint.textAlign = Paint.Align.LEFT // 文本对齐方式，可以根据需要调整
        // 计算文本框的位置，例如放在屏幕的右上角
        val scoreTextX: Float = 50.toFloat()
        val scoreTextY = 100f // 上边距屏幕顶部留空
        val lifeTextY = scoreTextY + 50 // 生命值文本放在分数下方

        // 绘制分数
        val scoreString = "Score: $score"
        canvas!!.drawText(scoreString, scoreTextX, scoreTextY, mPaint)

        // 绘制生命值，假设英雄机的生命值是通过HeroAircraft.getHp()获取
        val heroHp = HeroAircraft.hp
        val lifeString = "Life: $heroHp"
        canvas!!.drawText(lifeString, scoreTextX, lifeTextY, mPaint)
    }
    private fun paintOnlineScore() {
        /**TODO:动态绘制文本框显示英雄机的分数和生命值 */
        // 设置画笔属性，如颜色、字体大小、样式等
        mPaint.setColor(Color.RED)
        mPaint.textSize = 50f // 文字大小，根据实际情况调整
        mPaint.textAlign = Paint.Align.LEFT // 文本对齐方式，可以根据需要调整
        // 计算文本框的位置，例如放在屏幕的右上角
        val scoreTextX: Float = (GameActivity.screenWidth -  500).toFloat()
        val scoreTextY = 100f // 上边距屏幕顶部留空

        // 绘制分数
        val scoreString = "OnlineScore: $otherScore"
        canvas!!.drawText(scoreString, scoreTextX, scoreTextY, mPaint)

    }

    private fun musicControl() {
        if (GameActivity.musicState) {
            if (existBoss()) {
                if (bgmP.isPlaying) bgmP.pause()
                if (!bossBgmP.isPlaying) {
                    bossBgmP.start()
                    bossBgmP.isLooping = true
                }
            } else {
                if (!bgmP.isPlaying) {
                    bgmP.start()
                    bgmP.isLooping = true
                }
                if (bossBgmP.isPlaying) bossBgmP.pause()
            }
        }
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        Thread(this).start()
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        GameActivity.screenWidth = i1
        GameActivity.screenHeight = i2
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {

    }

    override fun run() {
        draw()
        while (mbLoop) {
            action()
            draw()
        }
    }

    companion object {
        const val TAG = "BaseGame"
    }
}
