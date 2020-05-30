package ru.psu.view

import ru.psu.view.auxiliaries.ColorDto
import ru.psu.view.auxiliaries.FontDto
import ru.psu.view.auxiliaries.shapes.ShapeDto
import ru.psu.view.auxiliaries.StrokeDto
import java.io.Serializable
import java.util.*

//Класс, соответствующий графическому представлению одной отдельно взятой конструкции
class ConstructView(
        val id: UUID, //Id графического представления
        var associatedConstructId:UUID //Id конструкции, которой соответствует отображение
):Serializable {
    var backColor:ColorDto = ColorDto(1.0, 1.0, 1.0) //Основной цвет граф. представления
    var content:String = "" //Текстовая подпись
    var font: FontDto? = null //Шрифт
    var shape: ShapeDto? = null //Контур графического представления
    var stroke: StrokeDto? = null //Тип границ контура (сплошные / прерывистые)
    var strokeColor:ColorDto = ColorDto(0.0, 0.0, 0.0) //Цвет контура графического представления
}