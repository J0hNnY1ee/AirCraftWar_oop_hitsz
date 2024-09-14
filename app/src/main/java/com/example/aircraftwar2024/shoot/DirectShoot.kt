package com.example.aircraftwar2024.shoot

import com.example.aircraftwar2024.aircraft.AbstractAircraft
import com.example.aircraftwar2024.aircraft.HeroAircraft
import com.example.aircraftwar2024.bullet.AbstractBullet
import com.example.aircraftwar2024.bullet.EnemyBullet
import com.example.aircraftwar2024.bullet.HeroBullet
import java.util.LinkedList

/**
 * 直线射击策略
 * 子弹与飞机同向沿直线飞行
 *
 * @author hitsz
 */
class DirectShoot : ShootStrategy {
    override fun shootWithStrategy(abstractAircraft: AbstractAircraft): List<AbstractBullet> {
        val res: MutableList<AbstractBullet> = LinkedList()
        val num = abstractAircraft.shootNum
        val power = abstractAircraft.power
        val direction = abstractAircraft.direction
        val rate = abstractAircraft.rate
        val isHero = abstractAircraft is HeroAircraft
        val x = abstractAircraft.locationX
        val y = abstractAircraft.locationY + abstractAircraft.height * direction / 2
        val speedX = 0
        val speedY = (abstractAircraft.speedY * rate + direction * 4).toInt()
        var bullet: AbstractBullet
        for (i in 0 until num) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            bullet = if (isHero) {
                HeroBullet(x + (i * 2 - num + 1) * 25, y, speedX, speedY, power)
            } else {
                EnemyBullet(x + (i * 2 - num + 1) * 25, y, speedX, speedY, power)
            }
            res.add(bullet)
        }
        return res
    }
}
