package ru.psu.view.auxiliaries.shapes

class PointDto(var x:Double = 0.0, var y:Double = 0.0) {
    operator fun plus(point:PointDto):PointDto { return PointDto(x + point.x, y + point.y) }
    operator fun minus(point:PointDto):PointDto { return PointDto(x - point.x, y - point.y) }
    fun copy(newX:Double = x, newY:Double = y):PointDto { return PointDto(newX, newY) }
}