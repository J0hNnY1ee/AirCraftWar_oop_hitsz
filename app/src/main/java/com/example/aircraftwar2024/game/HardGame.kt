package com.example.aircraftwar2024.game

import android.content.Context
import com.example.aircraftwar2024.ImageManager
import com.example.aircraftwar2024.aircraft.AbstractEnemyAircraft

class HardGame(context: Context) : BaseGame(context) {
    init {
        backGround = ImageManager.BACKGROUND3_IMAGE
        enemyMaxNumber = 5
        heroShootCycle = 8.0
        enemyShootCycle = 18.0
        eliteProb = 0.2
        bossScoreThreshold = 500
        tickCycle = 250
    }

    /**
     * 困难模式随着时间增加而提高难度
     */
    override fun tick() {
        tickCounter++
        if (tickCounter >= tickCycle) {
            tickCounter = 0
            // 困难模式增加：提高精英敌机出现概率
            if (eliteProb <= 1) {
                this.eliteProb *= 1.02
            }
            // 提高敌机产生频率（减小产生周期）
            this.enemyCycle *= 0.98

            // 提高敌机血量和速度
            this.gameLevel *= 1.02
            System.out.format(
                " 提高难度！精英机概率:%.2f,敌机周期:%.2f, 敌机属性提升倍率:%.2f。\n",
                eliteProb, enemyCycle, gameLevel
            )
        }
    }

    /**
     * 困难模式每次召唤BOSS，提高BOSS血量
     * @return
     */
    override fun produceBoss(): List<AbstractEnemyAircraft?> {
        val res = super.produceBoss()
        if (res!!.isNotEmpty()) {
            bossLevel *= 1.1
            System.out.format("Boss敌机血量倍率:%.2f。\n", bossLevel)
        }
        return res
    }
}
