package ru.psu.view.auxiliaries.shapes

class StrokeDto(var width:Double, var miterLimit:Double = 1.0,
                var cap:CAP = CAP.BUTT, var join:JOIN = JOIN.BEVEL,
                var dashArray:DoubleArray? = null) {
    enum class CAP { BUTT, ROUND, SQUARE }
    enum class JOIN { BEVEL, MITER, ROUND }
}