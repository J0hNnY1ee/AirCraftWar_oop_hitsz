package com.example.aircraftwar2024.supply

/**
 * 炸弹道具，自动触发
 *
 *
 * 使用效果：清除界面上除BOSS机外的所有敌机（包括子弹）
 *
 *
 * 【观察者模式】
 *
 * @author hitsz
 */
class BombSupply(locationX: Int, locationY: Int, speedX: Int, speedY: Int) :
    AbstractFlyingSupply(locationX, locationY, speedX, speedY) {
    override fun activate() {
        println("BombSupply active")
    }
}
