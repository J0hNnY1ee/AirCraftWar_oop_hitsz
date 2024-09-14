package com.example.aircraftwar2024.game

import android.content.Context
import com.example.aircraftwar2024.ImageManager
import com.example.aircraftwar2024.aircraft.AbstractEnemyAircraft
import java.util.LinkedList

class EasyGame(context: Context) : BaseGame(context) {
    init {
        backGround = ImageManager.BACKGROUND1_IMAGE
        enemyMaxNumber = 2
    }

    override fun tick() {}

    /**
     * 简单模式没有 boss
     * @return
     */
    override fun produceBoss(): List<AbstractEnemyAircraft?>? {
        return LinkedList()
    }
}
