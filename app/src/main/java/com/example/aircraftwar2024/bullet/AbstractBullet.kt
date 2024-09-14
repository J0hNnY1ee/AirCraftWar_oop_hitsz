package com.example.aircraftwar2024.bullet

import com.example.aircraftwar2024.activity.GameActivity
import com.example.aircraftwar2024.basic.AbstractFlyingObject

/**
 * 子弹类。
 * 也可以考虑不同类型的子弹
 *
 * @author hitsz
 */
open class AbstractBullet(
    locationX: Int,
    locationY: Int,
    speedX: Int,
    speedY: Int,
    val power: Int
) : AbstractFlyingObject(locationX, locationY, speedX, speedY) {

    override fun forward() {
        super.forward()

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= GameActivity.Companion.screenWidth) {
            vanish()
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= GameActivity.Companion.screenHeight) {
            // 向下飞行出界
            vanish()
        } else if (locationY <= 0) {
            // 向上飞行出界
            vanish()
        }
    }
}
