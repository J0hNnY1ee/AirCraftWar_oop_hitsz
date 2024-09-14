package com.example.aircraftwar2024.supply

import com.example.aircraftwar2024.aircraft.HeroAircraft
import com.example.aircraftwar2024.shoot.ShootStrategy
import java.util.Stack

/**
 * 火力道具：增加同时发射的子弹数。
 * 自动触发
 *
 * @author hitsz
 */
class FireSupply(locationX: Int, locationY: Int, speedX: Int, speedY: Int) :
    AbstractFlyingSupply(locationX, locationY, speedX, speedY) {
    @Throws(InterruptedException::class)
    override fun activate() {
        /**
         * 火力道具生效时，子弹数量在原有基础上+1，可叠加
         */
        val fireUpTask = Runnable {
            try {
                val shootStrategy: ShootStrategy? =
                    HeroAircraft.shootStrategy // 英雄机当前射击策略
                val oldNum: Int = HeroAircraft.shootNum
                shootNumStack!!.push(oldNum)

                // 同时射出的子弹数 +1
                val newNum = oldNum + 1
                HeroAircraft.shootNum =newNum
                Thread.sleep(10000) // 持续10s，之后恢复原有子弹数量
                HeroAircraft.shootNum = shootNumStack!!.pop()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        Thread(fireUpTask).start()
        println("FireSupply active")
    }

    companion object {
        /**
         * 用 Stack 存储旧的 shootNum 值，从而在计时结束后顺序恢复。
         * java.util.Stack 继承自 Vector，线程安全。
         */
        private var shootNumStack: Stack<Int>? = null

        init {
            // 初始化静态变量，所有线程共享该stack
            shootNumStack = Stack()
        }
    }
}
