package com.example.aircraftwar2024.aircraft

import com.example.aircraftwar2024.basic.AbstractFlyingObject
import com.example.aircraftwar2024.bullet.AbstractBullet
import com.example.aircraftwar2024.shoot.ShootStrategy

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
abstract class AbstractAircraft(
    locationX: Int,
    locationY: Int,
    speedX: Int,
    speedY: Int,
    protected var _hp: Int = 0

) : AbstractFlyingObject(locationX, locationY, speedX, speedY) {
    /**
     * 生命值
     */
    var hp: Int
        get() = _hp
        set(value) {
            if (value >= 0) {
                _hp = value
            } else {
               // throw IllegalArgumentException("HP cannot be negative")
            }
        }
    var maxHp: Int
        protected set

    /** 攻击方式
     * shootNum: 子弹一次发射数量
     * power:子弹伤害
     * direction:子弹射击方向 (向上发射：1，向下发射：-1)
     * rate: 调节子弹移动速度
     * shootStrategy: 攻击策略     * shootStrategy: 攻击策略
     */
    var shootNum = 1
    var power = 30
    var direction = -1
    var rate = 3.0
    /**
     * 获得飞机的射击策略
     *
     * @return ShootStrategy 实现对象
     */
    /**
     * 为可射击飞机指定射击策略
     * 【策略模式】
     *
     * @param shootStrategy ShootStrategy 接口的实现类
     */
    var shootStrategy: ShootStrategy? = null

    init {
        maxHp = hp
    }

    /**
     * 减少HP
     * 一般在飞机被攻击时调用，减少一部分HP
     *
     * @param decrease HP的减少量，应为非负值
     */
    fun decreaseHp(decrease: Int) {
        _hp -= decrease
        if (_hp <= 0) {
            _hp = 0
            vanish()
        }
    }

    /**
     * 增加HP
     * 一般在飞机获得补给时调用，增加部分HP，
     * 但不会使得飞机的HP超过初始值
     *
     * @param increase HP的增加量，应为非负值
     */
    fun increaseHp(increase: Int) {
        if (increase <= 0) {
            return
        }
        _hp += increase
        if (_hp > maxHp) {
            _hp = maxHp
        }
    }

//    fun getHp(): Int {
//        return hp
//    }
//
//    fun setHp(hp: Int) {
//        var hp = hp
//        this.hp = hp
//        if (hp > maxHp) {
//            hp = maxHp
//        }
//    }

    /**
     * 飞机射击方法，可射击对象必须实现
     * @return
     * 可射击对象需实现，返回子弹
     * 非可射击对象空实现，返回null
     */
    open fun shoot(): List<AbstractBullet?>? {
        assert(shootStrategy != null) { "Shoot strategy unset!" }
        return shootStrategy!!.shootWithStrategy(this)
    }
}
