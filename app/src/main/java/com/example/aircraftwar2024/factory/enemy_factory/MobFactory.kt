package com.example.aircraftwar2024.factory.enemy_factory

import com.example.aircraftwar2024.ImageManager
import com.example.aircraftwar2024.activity.GameActivity
import com.example.aircraftwar2024.aircraft.AbstractEnemyAircraft
import com.example.aircraftwar2024.aircraft.MobEnemy

class MobFactory : EnemyFactory {
    private val mobHp = 30
    private val speedY = 10
    override fun createEnemyAircraft(level: Double): AbstractEnemyAircraft {
        return MobEnemy(
            (Math.random() * (GameActivity.Companion.screenWidth - ImageManager.MOB_ENEMY_IMAGE!!.getWidth())).toInt(),
            (Math.random() * GameActivity.Companion.screenHeight * 0.05).toInt(),
            0,
            (speedY * level).toInt(),
            (mobHp * level).toInt()
        )
    }
}
