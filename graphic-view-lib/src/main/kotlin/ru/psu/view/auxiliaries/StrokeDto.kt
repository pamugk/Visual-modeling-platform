package ru.psu.view.auxiliaries

class StrokeDto(var width:Double, var miterLimit:Double = 1.0,
                var cap: Cap = Cap.BUTT, var join: Join = Join.BEVEL,
                var dashArray:DoubleArray? = null) {
    enum class Cap { BUTT, ROUND, SQUARE }
    enum class Join { BEVEL, MITER, ROUND }
    fun copy(
            newWidth:Double = width, newMiterLimit:Double = miterLimit, newCap: Cap = cap, newJoin:Join = join,
            newDashArray:DoubleArray? = dashArray?.clone()):StrokeDto {
        return StrokeDto(newWidth, newMiterLimit, newCap, newJoin, newDashArray)
    }
}