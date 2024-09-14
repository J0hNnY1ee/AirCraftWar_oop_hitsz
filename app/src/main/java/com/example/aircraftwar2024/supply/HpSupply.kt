package com.example.aircraftwar2024.supply

import com.example.aircraftwar2024.aircraft.HeroAircraft

/**
 * 加血道具：增加HP值。
 * 自动触发
 *
 * @author hitsz
 */
class HpSupply(locationX: Int, locationY: Int, speedX: Int, speedY: Int) :
    AbstractFlyingSupply(locationX, locationY, speedX, speedY) {
    private val increasedHp = 30
    override fun activate() {
        HeroAircraft.increaseHp(increasedHp)
        println("HpSupply active")
    }
}
