package com.example.aircraftwar2024.game

import android.content.Context
import com.example.aircraftwar2024.ImageManager

class MediumGame(context: Context) : BaseGame(context) {
    init {
        backGround = ImageManager.BACKGROUND2_IMAGE
        enemyMaxNumber = 3
        heroShootCycle = 9.0
        enemyShootCycle = 19.0
        eliteProb = 0.15
        bossScoreThreshold = 300
        tickCycle = 300
    }

    /**
     * 普通模式随着时间增加而提高难度
     */
    override fun tick() {
        tickCounter++
        if (tickCounter >= tickCycle) {
            tickCounter = 0
            // 提高敌机产生频率（减小产生周期）
            enemyCycle *= 0.99
            // 提高敌机血量
            gameLevel *= 1.01
            System.out.format(
                " 提高难度！精英机概率:%.2f,敌机周期:%.2f, 敌机属性提升倍率:%.2f。\n",
                eliteProb, enemyCycle, gameLevel
            )
        }
    }
}
