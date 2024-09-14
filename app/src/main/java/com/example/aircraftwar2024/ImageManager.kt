package com.example.aircraftwar2024

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.aircraftwar2024.aircraft.BossEnemy
import com.example.aircraftwar2024.aircraft.EliteEnemy
import com.example.aircraftwar2024.aircraft.HeroAircraft
import com.example.aircraftwar2024.aircraft.MobEnemy
import com.example.aircraftwar2024.bullet.EnemyBullet
import com.example.aircraftwar2024.bullet.HeroBullet
import com.example.aircraftwar2024.supply.BombSupply
import com.example.aircraftwar2024.supply.FireSupply
import com.example.aircraftwar2024.supply.HpSupply

object ImageManager {
    /**
     * 类名-图片 映射，存储各基类的图片 <br></br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private val CLASSNAME_IMAGE_MAP: MutableMap<String, Bitmap?> = HashMap()
    var BACKGROUND1_IMAGE: Bitmap? = null
    var BACKGROUND2_IMAGE: Bitmap? = null
    var BACKGROUND3_IMAGE: Bitmap? = null
    var HERO_IMAGE: Bitmap? = null
    var HERO_BULLET_IMAGE: Bitmap? = null
    var ENEMY_BULLET_IMAGE: Bitmap? = null
    var MOB_ENEMY_IMAGE: Bitmap? = null
    var ELITE_ENEMY_IMAGE: Bitmap? = null
    var BOSS_ENEMY_IMAGE: Bitmap? = null
    var FIRE_SUPPLY_IMAGE: Bitmap? = null
    var HP_SUPPLY_IMAGE: Bitmap? = null
    var BOMB_SUPPLY_IMAGE: Bitmap? = null
    var BACKGROUND1_bg: Bitmap? = null
    fun initImage(context: Context) {
        BACKGROUND1_IMAGE = BitmapFactory.decodeResource(context.resources, R.drawable.bg)
        HERO_IMAGE = BitmapFactory.decodeResource(context.resources, R.drawable.hero)
        BACKGROUND2_IMAGE = BitmapFactory.decodeResource(context.resources, R.drawable.bg2)
        BACKGROUND3_IMAGE = BitmapFactory.decodeResource(context.resources, R.drawable.bg3)
        MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(context.resources, R.drawable.mob)
        ELITE_ENEMY_IMAGE = BitmapFactory.decodeResource(context.resources, R.drawable.elite)
        BOSS_ENEMY_IMAGE = BitmapFactory.decodeResource(context.resources, R.drawable.boss)
        HERO_BULLET_IMAGE = BitmapFactory.decodeResource(context.resources, R.drawable.bullet_hero)
        ENEMY_BULLET_IMAGE =
            BitmapFactory.decodeResource(context.resources, R.drawable.bullet_enemy)
        FIRE_SUPPLY_IMAGE = BitmapFactory.decodeResource(context.resources, R.drawable.prop_bullet)
        HP_SUPPLY_IMAGE = BitmapFactory.decodeResource(context.resources, R.drawable.prop_blood)
        BOMB_SUPPLY_IMAGE = BitmapFactory.decodeResource(context.resources, R.drawable.prop_bomb)
        CLASSNAME_IMAGE_MAP[HeroAircraft::class.java.getName()] = HERO_IMAGE
        CLASSNAME_IMAGE_MAP[MobEnemy::class.java.getName()] = MOB_ENEMY_IMAGE
        CLASSNAME_IMAGE_MAP[HeroBullet::class.java.getName()] = HERO_BULLET_IMAGE
        CLASSNAME_IMAGE_MAP[EnemyBullet::class.java.getName()] = ENEMY_BULLET_IMAGE
        CLASSNAME_IMAGE_MAP[EliteEnemy::class.java.getName()] =
            ELITE_ENEMY_IMAGE
        CLASSNAME_IMAGE_MAP[BossEnemy::class.java.getName()] = BOSS_ENEMY_IMAGE
        CLASSNAME_IMAGE_MAP[FireSupply::class.java.getName()] = FIRE_SUPPLY_IMAGE
        CLASSNAME_IMAGE_MAP[HpSupply::class.java.getName()] = HP_SUPPLY_IMAGE
        CLASSNAME_IMAGE_MAP[BombSupply::class.java.getName()] = BOMB_SUPPLY_IMAGE
    }

    operator fun get(className: String): Bitmap? {
        return CLASSNAME_IMAGE_MAP[className]
    }

    operator fun get(obj: Any?): Bitmap? {
        return if (obj == null) {
            null
        } else ImageManager[obj.javaClass.getName()]
    }
}
