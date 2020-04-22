package ru.psu.view

import java.awt.Color
import java.awt.Font
import java.awt.Shape
import java.awt.Stroke
import java.util.*

//Класс, соответствующий графическому представлению одной отдельно взятой конструкции
class ConstructView(
        val id: UUID, //Id графического представления
        var associatedConstructId:UUID //Id конструкции, которой соответствует отображение
) {
    var backColor:Color = Color.WHITE //Основной цвет граф. представления
    var content:String = "" //Текстовая подпись
    var font:Font? = null //Текстовая подпись
    var shape:Shape? = null //Контур графического представления
    var stroke: Stroke? = null //Тип границ контура (сплошные / прерывистые)
    var strokeColor:Color = Color.BLACK //Цвет контура графического представления
}