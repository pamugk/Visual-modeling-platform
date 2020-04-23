package ru.psu.view.auxiliaries.shapes

enum class Shapes {
    Arc, Circle, CubicCurve, Ellipse, Line, Path, Polygon, QuadCurve, Rectangle,
}

abstract class ShapeDto(val shape:Shapes)