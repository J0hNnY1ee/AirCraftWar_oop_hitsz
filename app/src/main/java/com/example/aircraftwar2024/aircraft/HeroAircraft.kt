package com.example.aircraftwar2024.aircraft

import com.example.aircraftwar2024.ImageManager
import com.example.aircraftwar2024.activity.GameActivity
import com.example.aircraftwar2024.shoot.DirectShoot
import kotlin.concurrent.Volatile

/**
 * 英雄飞机，游戏玩家操控，遵循单例模式（singleton)
 * 【单例模式】
 * @author hitsz
 */
object HeroAircraft  : AbstractAircraft(
    GameActivity.screenWidth / 2,
    GameActivity.screenHeight - ImageManager.HERO_IMAGE!!.getHeight(),
    0,
    0,
    1000
) {
    /**
     * 单例模式：私有化构造方法
     */
    init {
        shootNum = 1
        power = 30
        direction = -1
        rate = 3.0
        shootStrategy = DirectShoot()
    }

    override fun forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }
    fun reset()
    {
        isValid = true
        shootNum = 1
        power = 30
        direction = -1
        rate = 3.0
        shootStrategy = DirectShoot()
        locationX =  GameActivity.screenWidth / 2
        locationY =  GameActivity.screenHeight - ImageManager.HERO_IMAGE!!.getHeight()
        speedX =  0
        speedY = 0
        _hp = 1000
    }
}
