package ru.psu.view.auxiliaries.shapes

class ArcDto(
        var centerX:Double, var centerY:Double,
        var radiusX:Double, var radiusY:Double,
        var startAngle:Double, var length:Double):ShapeDto(Shapes.Arc)