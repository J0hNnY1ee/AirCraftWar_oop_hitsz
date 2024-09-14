package com.example.aircraftwar2024.factory.supply_factory

import com.example.aircraftwar2024.supply.AbstractFlyingSupply
import com.example.aircraftwar2024.supply.HpSupply

/**
 * 加血道具工厂
 *
 * @author hitsz
 */
class HpSupplyFactory : SupplyFactory {
    override fun createFlyingSupply(x: Int, y: Int): AbstractFlyingSupply {
        return HpSupply(x, y, 0, 2)
    }
}
