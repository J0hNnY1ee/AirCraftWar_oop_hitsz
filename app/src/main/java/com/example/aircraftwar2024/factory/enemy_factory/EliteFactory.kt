package com.example.aircraftwar2024.factory.enemy_factory

import com.example.aircraftwar2024.ImageManager
import com.example.aircraftwar2024.activity.GameActivity
import com.example.aircraftwar2024.aircraft.AbstractEnemyAircraft
import com.example.aircraftwar2024.aircraft.EliteEnemy
import com.example.aircraftwar2024.shoot.DirectShoot

class EliteFactory : EnemyFactory {
    private val eliteHp = 60
    private val speedY = 5
    override fun createEnemyAircraft(level: Double): AbstractEnemyAircraft {
        val elite = EliteEnemy(
            (Math.random() * (GameActivity.Companion.screenWidth - ImageManager.ELITE_ENEMY_IMAGE!!.getWidth())).toInt(),
            (Math.random() * GameActivity.Companion.screenHeight * 0.05).toInt(),
            ((Math.random() - 0.5) * 20).toInt(),
            (speedY * level).toInt(),
            (eliteHp * level).toInt()
        )
        elite.shootStrategy = DirectShoot()
        return elite
    }
}
