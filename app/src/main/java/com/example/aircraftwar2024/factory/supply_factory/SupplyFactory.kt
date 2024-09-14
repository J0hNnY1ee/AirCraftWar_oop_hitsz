package com.example.aircraftwar2024.factory.supply_factory

import com.example.aircraftwar2024.supply.AbstractFlyingSupply

/**
 * 道具工厂接口
 * 【工厂模式】
 *
 * @author hitsz
 */
interface SupplyFactory {
    /**
     * 道具工厂
     *
     * @param x          基准x坐标
     * @param y          基准y坐标
     * @return 新生成的道具
     */
    fun createFlyingSupply(x: Int, y: Int): AbstractFlyingSupply
}
