package com.example.aircraftwar2024.basic

import android.graphics.Bitmap
import com.example.aircraftwar2024.ImageManager
import com.example.aircraftwar2024.activity.GameActivity
import com.example.aircraftwar2024.aircraft.AbstractAircraft

/**
 * 可飞行对象的父类
 *
 * @author hitsz
 */
abstract class AbstractFlyingObject(
    /**
     * x 轴坐标
     */
    var locationX: Int,
    /**
     * y 轴坐标
     */
    var locationY: Int,
    /**
     * x 轴移动速度
     */
    var speedX: Int,
    /**
     * y 轴移动速度
     */
    var speedY: Int
) {

    //locationX、locationY为图片中心位置坐标

    /**
     * 图片,
     * null 表示未设置
     */
    var image: Bitmap? = ImageManager[this]

    /**
     * x 轴长度，根据图片尺寸获得
     * -1 表示未设置
     */
    var width = ImageManager[this]!!.getWidth()

    /**
     * y 轴长度，根据图片尺寸获得
     * -1 表示未设置
     */
    var height = ImageManager[this]!!.getHeight()

    /**
     * 有效（生存）标记，
     * 通常标记为 false的对象会再下次刷新时清除
     */
    protected var isValid = true

    /**
     * 可飞行对象根据速度移动
     * 若飞行对象触碰到横向边界，横向速度反向
     */
    open fun forward() {
        locationX += speedX
        locationY += speedY
        if (locationX <= 0 || locationX >= GameActivity.Companion.screenWidth) {
            // 横向超出边界后反向
            speedX = -speedX
        }
    }

    /**
     * 碰撞检测，当对方坐标进入我方范围，判定我方击中<br></br>
     * 对方与我方覆盖区域有交叉即判定撞击。
     * <br></br>
     * 非飞机对象区域：
     * 横向，[x - width/2, x + width/2]
     * 纵向，[y - height/2, y + height/2]
     * <br></br>
     * 飞机对象区域：
     * 横向，[x - width/2, x + width/2]
     * 纵向，[y - height/4, y + height/4]
     *
     * @param flyingObject 撞击对方
     * @return true: 我方被击中; false 我方未被击中
     */
    fun crash(flyingObject: AbstractFlyingObject): Boolean {
        // 缩放因子，用于控制 y轴方向区域范围
        val factor = if (this is AbstractAircraft) 2 else 1
        val fFactor = if (flyingObject is AbstractAircraft) 2 else 1
        val x = flyingObject.locationX
        val y = flyingObject.locationY
        val fWidth = flyingObject.width
        val fHeight = flyingObject.height
        return x + (fWidth + height) / 2 > locationX && x - (fWidth + width) / 2 < locationX && y + (fHeight / fFactor + height / factor) / 2 > locationY && y - (fHeight / fFactor + height / factor) / 2 < locationY
    }

    //    fun setLocationX(locationX: Double) {
//        this.locationX = locationX.toInt()
//    }
//
//    fun setLocationY(locationY: Double) {
//        this.locationY = locationY.toInt()
//    }
//
    fun setLocation(locationX: Double, locationY: Double) {
        this.locationX = locationX.toInt()
        this.locationY = locationY.toInt()
    }

    fun setSpeedX(speedX: Double) {
        this.speedX = speedX.toInt()
    }

    fun setSpeedY(speedY: Double) {
        this.speedY = speedY.toInt()
    }

//     fun getImage(): Bitmap? {
//        if (image == null) {
//            image = ImageManager[this]
//        }
//        return image
//    }
//
//    fun getWidth(): Int {
//        if (width == -1) {
//            // 若未设置，则查询图片宽度并设置
//            width = ImageManager[this]!!.getWidth()
//        }
//        return width
//    }
//
//    fun getHeight(): Int {
//        if (height == -1) {
//            // 若未设置，则查询图片高度并设置
//            height = ImageManager.get(this)!!.getHeight()
//        }
//        return height
//    }

    fun notValid(): Boolean {
        return !isValid
    }

    /**
     * 标记消失，
     * isValid = false.
     * notValid() => true.
     */
    fun vanish() {
        isValid = false
    }
}
