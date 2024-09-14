package com.example.aircraftwar2024.aircraft

import com.example.aircraftwar2024.activity.GameActivity
import com.example.aircraftwar2024.supply.AbstractFlyingSupply

/**
 * 敌机父类
 * 敌机：BOSS, ELITE, MOB
 *
 * @author hitsz
 */
abstract class AbstractEnemyAircraft(
    locationX: Int,
    locationY: Int,
    speedX: Int,
    speedY: Int,
    hp: Int
) : AbstractAircraft(locationX, locationY, speedX, speedY, hp) {
    /**
     * 获得敌机分数，击毁敌机时，调用该方法获得分数。
     * @return 敌机的分数
     */
    abstract fun score(): Int

    /**
     * 敌机坠毁时以一定概率掉落道具
     * @return 道具
     */
    abstract fun generateSupplies(): List<AbstractFlyingSupply?>

    /**
     * 敌机向下飞行出界后，标记无效
     */
    override fun forward() {
        super.forward()
        // 判定 y 轴向下飞行出界
        if (locationY >= GameActivity.Companion.screenHeight) {
            vanish()
        }
    }
}
