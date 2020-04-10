package ru.psu.view

import java.awt.Color
import java.awt.Font
import java.awt.Shape
import java.awt.Stroke
import java.util.*

class ConstructView(val id: UUID, var associatedConstructId:UUID) {
    var backColor:Color = Color.WHITE
    var content:String = ""
    var font:Font = Font.getFont(Font.SANS_SERIF)
    val shape:Shape? = null
    var stroke: Stroke? = null
    var strokeColor:Color = Color.BLACK
}