package com.example.aircraftwar2024.factory.enemy_factory

import com.example.aircraftwar2024.ImageManager
import com.example.aircraftwar2024.activity.GameActivity
import com.example.aircraftwar2024.aircraft.AbstractEnemyAircraft
import com.example.aircraftwar2024.aircraft.BossEnemy
import com.example.aircraftwar2024.shoot.DisperseShoot

/**
 *
 *
 * @author hitsz
 */
class BossFactory : EnemyFactory {
    private val bossHp = 500
    override fun createEnemyAircraft(bosslevel: Double): AbstractEnemyAircraft {
        val boss = BossEnemy(
            (GameActivity.Companion.screenWidth - ImageManager.BOSS_ENEMY_IMAGE!!.getWidth()) / 2,
            ImageManager.BOSS_ENEMY_IMAGE!!.getHeight() / 2,
            5,
            0, (bossHp * bosslevel).toInt()

        )
        boss.shootStrategy = DisperseShoot()
        return boss
    }
}
