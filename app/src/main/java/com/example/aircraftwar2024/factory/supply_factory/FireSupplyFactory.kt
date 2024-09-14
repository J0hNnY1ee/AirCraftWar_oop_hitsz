package com.example.aircraftwar2024.factory.supply_factory

import com.example.aircraftwar2024.supply.AbstractFlyingSupply
import com.example.aircraftwar2024.supply.FireSupply

/**
 * 火力道具工厂
 *
 * @author hitsz
 */
class FireSupplyFactory : SupplyFactory {
    override fun createFlyingSupply(x: Int, y: Int): AbstractFlyingSupply {
        return FireSupply(x, y, 0, 2)
    }
}
