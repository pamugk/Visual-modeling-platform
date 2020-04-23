package ru.psu.utils

import javafx.scene.paint.Color
import javafx.scene.shape.*
import javafx.scene.text.Font
import javafx.scene.text.Text
import ru.psu.view.ConstructView
import tornadofx.*
import java.awt.BasicStroke
import java.awt.BasicStroke.*
import java.awt.geom.*

//Функция для построения JavaFX-компонента по описанию графического представления конструкции
fun constructShape(view:ConstructView):Shape {
    if (view.shape == null)
        return Circle()
    val shape:Shape = transformShape(view.shape!!)
    shape.fill = view.backColor.transform()
    shape.stroke = view.strokeColor.transform()
    if (view.stroke == null || view.stroke !is BasicStroke)
        return shape
    val stroke = view.stroke as BasicStroke
    if (stroke.dashArray != null)
        stroke.dashArray.forEach { shape.strokeDashArray.add(it.toDouble()) }
    shape.strokeLineCap = getStrokeLineCap(stroke)
    shape.strokeLineJoin = getStrokeLineJoin(stroke)
    shape.strokeMiterLimit = stroke.miterLimit.toDouble()
    shape.strokeWidth = stroke.lineWidth.toDouble()
    return shape
}

//Функция для построения JavaFX-текста, сопровождающего конструкцию
fun constructText(view:ConstructView): Text {
    val text = Text(view.content)
    text.font = if (view.font == null) Font.getDefault() else Font(view.font!!.name, view.font!!.size.toDouble())
    return text
}

fun Path.cubiccurveTo(controlX1:Double, controlY1:Double,
                      controlX2:Double, controlY2:Double,
                      endX:Double, endY:Double) {
    val cubiccurve = CubicCurveTo()
    cubiccurve.controlX1 = controlX1; cubiccurve.controlY1 = controlY1
    cubiccurve.controlX2 = controlX2; cubiccurve.controlY2 = controlY2
    cubiccurve.x = endX; cubiccurve.y = endY
    this.elements.add(cubiccurve)
}

//Функция для преобразования цвета из AWT в цвет из JavaFX
fun java.awt.Color.transform():Color = Color.rgb(this.red, this.green, this.blue, this.alpha / 255.0)

//Функция для преобразования цвета из JavaFX в цвет из AWT
fun Color.transform():java.awt.Color =
        java.awt.Color(this.red.toFloat(), this.green.toFloat(), this.blue.toFloat(), this.opacity.toFloat())

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

//Метод для преобразования формы по стандарту AWT в форму по стандарту JavaFX
fun transformShape(shape:java.awt.Shape):Shape =
        when (shape) {
            is Arc2D -> Arc(shape.centerX, shape.centerY, shape.maxX - shape.minX,
                    shape.maxY - shape.minY, shape.angleStart, shape.angleExtent)
            is CubicCurve2D -> CubicCurve(shape.x1, shape.y1, shape.ctrlX1, shape.ctrlY1, shape.ctrlX2,
                    shape.ctrlY2, shape.x2, shape.y2)
            is Ellipse2D -> Ellipse(shape.centerX, shape.centerY, shape.width, shape.height)
            is Line2D -> Line(shape.x1, shape.y1, shape.x2, shape.y2)
            is GeneralPath -> {
                val customPath = Path()
                customPath.fillRule = if(shape.windingRule == Path2D.WIND_EVEN_ODD) FillRule.EVEN_ODD else FillRule.NON_ZERO

                //В соответствии с определением интерфейса PathIterator,
                //для получения информации о текущем сегменте необходимо передать массив из 6 элементов,
                //в котором будет сохранено несколько точек, где каждая точка представлена двумя элементами - x и y.
                //Итого максимум может быть помещено 3 точки, минимум - 0 (зависит от типа сегмента
                val points = DoubleArray(6)
                val iterator = shape.getPathIterator(null)
                while (!iterator.isDone) {
                    when (iterator.currentSegment(points)) {
                        PathIterator.SEG_MOVETO -> customPath.moveTo(points[0], points[1])
                        PathIterator.SEG_LINETO -> customPath.lineTo(points[0], points[1])
                        PathIterator.SEG_CUBICTO -> customPath.cubiccurveTo(controlX1 = points[0], controlY1 = points[1],
                        controlX2 = points[2], controlY2 = points[3], endX = points[4], endY = points[5])
                        PathIterator.SEG_QUADTO -> customPath.quadqurveTo(points[0], points[1], points[2], points[3])
                        PathIterator.SEG_CLOSE -> customPath.closepath()
                    }
                    iterator.next()
                }
                customPath
            }
            is java.awt.Polygon -> {
                val polygon = Polygon()
                for (i in 0 until shape.npoints)
                    polygon.points.addAll(shape.xpoints[i].toDouble(), shape.ypoints[i].toDouble())
                polygon
            }
            is QuadCurve2D -> QuadCurve(shape.x1, shape.y1, shape.ctrlX, shape.ctrlY, shape.x2, shape.y2)
            is Rectangle2D -> Rectangle(shape.minX, shape.minY, shape.width, shape.height)
            is RoundRectangle2D -> {
                val roundedRectangle = Rectangle(shape.minX, shape.minY, shape.width, shape.height)
                roundedRectangle.arcHeight = shape.arcHeight
                roundedRectangle.arcWidth = shape.arcWidth
                roundedRectangle
            }
            else -> Circle()
        }