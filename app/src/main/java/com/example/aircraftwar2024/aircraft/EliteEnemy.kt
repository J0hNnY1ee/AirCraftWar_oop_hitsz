package com.example.aircraftwar2024.aircraft

import android.util.Log
import com.example.aircraftwar2024.factory.supply_factory.BombSupplyFactory
import com.example.aircraftwar2024.factory.supply_factory.FireSupplyFactory
import com.example.aircraftwar2024.factory.supply_factory.HpSupplyFactory
import com.example.aircraftwar2024.factory.supply_factory.SupplyFactory
import com.example.aircraftwar2024.supply.AbstractFlyingSupply
import java.util.LinkedList

/**
 * 精英敌机
 * 可射击
 *
 * @author hitsz
 */
class EliteEnemy(locationX: Int, locationY: Int, speedX: Int, speedY: Int, hp: Int) :
    AbstractEnemyAircraft(locationX, locationY, speedX, speedY, hp) {
    /**
     * 道具工厂
     */
    private var flyingSupplyFactory: SupplyFactory? = null

    init {
        shootNum = 1
        power = 10
        direction = 1
        rate = 1.2
    }

    override fun generateSupplies(): List<AbstractFlyingSupply?> {
        val res: MutableList<AbstractFlyingSupply?> = LinkedList()
        val prob = Math.random()
        if (prob < 0.3) {
            Log.d("EliteEnemy", "generate bomb supply")
            flyingSupplyFactory = BombSupplyFactory()
        } else if (prob < 0.5) {
            Log.d("EliteEnemy", "generate fire supply")
            flyingSupplyFactory = FireSupplyFactory()
        } else if (prob < 0.9) {
            Log.d("EliteEnemy", "generate hp supply")
            flyingSupplyFactory = HpSupplyFactory()
        } else {
            println("No supply generated!")
        }
        if (flyingSupplyFactory != null) {
            res.add(flyingSupplyFactory!!.createFlyingSupply(locationX, locationY))
        }
        return res
    }

    override fun score(): Int {
        return 30
    }
}
