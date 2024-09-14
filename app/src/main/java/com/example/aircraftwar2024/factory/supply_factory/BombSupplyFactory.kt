package com.example.aircraftwar2024.factory.supply_factory

import com.example.aircraftwar2024.supply.AbstractFlyingSupply
import com.example.aircraftwar2024.supply.BombSupply

/**
 * 炸弹道具工厂
 *
 * @author hitsz
 */
class BombSupplyFactory : SupplyFactory {
    override fun createFlyingSupply(x: Int, y: Int): AbstractFlyingSupply {
        return BombSupply(x, y, 0, 2)
    }
}
