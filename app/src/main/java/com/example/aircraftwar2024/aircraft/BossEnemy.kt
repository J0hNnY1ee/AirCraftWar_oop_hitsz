package com.example.aircraftwar2024.aircraft

import com.example.aircraftwar2024.factory.supply_factory.BombSupplyFactory
import com.example.aircraftwar2024.factory.supply_factory.FireSupplyFactory
import com.example.aircraftwar2024.factory.supply_factory.HpSupplyFactory
import com.example.aircraftwar2024.factory.supply_factory.SupplyFactory
import com.example.aircraftwar2024.supply.AbstractFlyingSupply
import java.util.LinkedList

/**
 * BOSS 敌机
 * 可射击
 * 工厂构造
 *
 * @author hitsz
 */
class BossEnemy(locationX: Int, locationY: Int, speedX: Int, speedY: Int, hp: Int) :
    AbstractEnemyAircraft(locationX, locationY, speedX, speedY, hp) {
    /**
     * 道具工厂
     */
    private var flyingSupplyFactory: SupplyFactory? = null

    init {
        shootNum = 3
        power = 10
        direction = 1
        rate = 1.2
    }

    override fun generateSupplies(): List<AbstractFlyingSupply?> {
        val res: MutableList<AbstractFlyingSupply?> = LinkedList()

        //boss机坠落随机掉落3个道具
        for (i in 0..2) {
            flyingSupplyFactory = null
            val prob = Math.random()
            if (prob < 0.3) {
                flyingSupplyFactory = BombSupplyFactory()
            } else if (prob < 0.5) {
                flyingSupplyFactory = FireSupplyFactory()
            } else if (prob < 0.8) {
                flyingSupplyFactory = HpSupplyFactory()
            } else {
                println("No supply generated!")
            }
            if (flyingSupplyFactory != null) {
                val dropLocationX =
                    if (locationX + 50 * i < 470) locationX + 50 * i else 470 - 50 * i
                res.add(flyingSupplyFactory!!.createFlyingSupply(dropLocationX, locationY))
            }
        }
        return res
    }

    override fun score(): Int {
        return 100
    }
}
