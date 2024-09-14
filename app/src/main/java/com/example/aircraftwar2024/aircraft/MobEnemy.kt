package com.example.aircraftwar2024.aircraft

import com.example.aircraftwar2024.bullet.AbstractBullet
import com.example.aircraftwar2024.supply.AbstractFlyingSupply
import java.util.LinkedList

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
class MobEnemy(locationX: Int, locationY: Int, speedX: Int, speedY: Int, hp: Int) :
    AbstractEnemyAircraft(locationX, locationY, speedX, speedY, hp) {
    /**
     * 获得敌机分数，击毁敌机时，调用该方法获得分数。
     * @return 敌机的分数
     */
    override fun score(): Int {
        return 10
    }

    override fun shoot(): List<AbstractBullet?>? {
        return LinkedList()
    }

    override fun generateSupplies(): List<AbstractFlyingSupply?> {
        return LinkedList()
    }
}
