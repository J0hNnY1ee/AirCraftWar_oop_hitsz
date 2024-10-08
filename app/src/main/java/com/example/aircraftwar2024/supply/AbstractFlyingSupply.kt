package com.example.aircraftwar2024.supply

import com.example.aircraftwar2024.activity.GameActivity
import com.example.aircraftwar2024.basic.AbstractFlyingObject

/**
 * 所有飞行道具的抽象父类。
 *
 * @author hitsz
 */
abstract class AbstractFlyingSupply(locationX: Int, locationY: Int, speedX: Int, speedY: Int) :
    AbstractFlyingObject(locationX, locationY, speedX, speedY) {
    /**
     * 道具向下飞行出界后，标记无效
     */
    override fun forward() {
        super.forward()
        // 判定 y 轴向下飞行出界
        if (locationY >= GameActivity.Companion.screenHeight) {
            vanish()
        }
    }

    /**
     * 道具生效
     * @param game
     */
    @Throws(InterruptedException::class)
    abstract fun activate()
}
