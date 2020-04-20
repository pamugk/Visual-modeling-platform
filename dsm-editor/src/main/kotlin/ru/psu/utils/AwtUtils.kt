package ru.psu.utils

import javafx.scene.paint.Color
import javafx.scene.shape.*
import javafx.scene.text.Font
import javafx.scene.text.Text
import ru.psu.view.ConstructView
import java.awt.BasicStroke
import java.awt.BasicStroke.*

//Функция для построения JavaFX-компонента по описанию графического представления конструкции
fun constructShape(view:ConstructView):SVGPath {
    val shape:SVGPath = SVGPath()
    if (view.shape == null)
        return shape
    shape.content = view.shape
    shape.fill = formColor(view.backColor)
    shape.stroke = formColor(view.strokeColor)
    if (view.stroke == null || view.stroke !is BasicStroke)
        return shape
    val stroke = view.stroke as BasicStroke
    stroke.dashArray.forEach { shape.strokeDashArray.add(it.toDouble()) }
    shape.strokeLineCap = getStrokeLineCap(stroke)
    shape.strokeLineJoin = getStrokeLineJoin(stroke)
    shape.strokeMiterLimit = stroke.miterLimit.toDouble()
    shape.strokeWidth = stroke.lineWidth.toDouble()
    return shape
}

//Функция для построения JavaFX-текста, сопровождающего конструкцию
fun constructText(view:ConstructView): Text {
    val text:Text = Text(view.content)
    text.font = Font(view.font.name, view.font.size.toDouble())
    return text
}

//Функция для преобразования цвета из AWT в цвет из JavaFX
fun formColor(color:java.awt.Color):Color = Color.rgb(color.red, color.green, color.blue, color.alpha / 255.0)

//Функция для определения стиля конца штриха в терминах JavaFX по описанию AWT
fun getStrokeLineCap(stroke:BasicStroke):StrokeLineCap =
        when (stroke.endCap) {
            CAP_ROUND -> StrokeLineCap.ROUND
            CAP_SQUARE -> StrokeLineCap.SQUARE
            else -> StrokeLineCap.BUTT
        }

fun getStrokeLineJoin(stroke:BasicStroke):StrokeLineJoin =
        when (stroke.lineJoin) {
            JOIN_BEVEL -> StrokeLineJoin.BEVEL
            JOIN_MITER -> StrokeLineJoin.MITER
            else -> StrokeLineJoin.ROUND
        }