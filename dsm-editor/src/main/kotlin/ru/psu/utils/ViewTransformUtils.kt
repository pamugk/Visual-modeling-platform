package ru.psu.utils

import javafx.scene.paint.Color
import javafx.scene.shape.*
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import ru.psu.view.ConstructView
import ru.psu.view.auxiliaries.ColorDto
import ru.psu.view.auxiliaries.FontDto
import ru.psu.view.auxiliaries.shapes.*
import tornadofx.*

//Функция для построения JavaFX-компонента по описанию графического представления конструкции
fun constructShape(view:ConstructView):Shape {
    if (view.shape == null)
        return Circle()
    val shape:Shape = view.shape!!.transform()
    shape.fill = view.backColor.transform()
    shape.stroke = view.strokeColor.transform()
    if (view.stroke == null)
        return shape
    if (view.stroke?.dashArray != null)
        for (dash in view.stroke!!.dashArray!!)
            shape.strokeDashArray.add(dash)
    shape.strokeLineCap = getStrokeLineCap(view.stroke!!)
    shape.strokeLineJoin = getStrokeLineJoin(view.stroke!!)
    shape.strokeMiterLimit = view.stroke!!.miterLimit
    shape.strokeWidth = view.stroke!!.width
    return shape
}

//Функция для построения JavaFX-текста, сопровождающего конструкцию
fun constructText(view:ConstructView): Text {
    val text = Text(view.content)
    text.font = view.font.transform()
    return text
}

fun getFontWeight(font:FontDto):FontWeight =
        when (font.weight) {
            FontDto.Weight.BLACK -> FontWeight.BLACK
            FontDto.Weight.BOLD -> FontWeight.BOLD
            FontDto.Weight.EXTRA_BOLD -> FontWeight.EXTRA_BOLD
            FontDto.Weight.EXTRA_LIGHT -> FontWeight.EXTRA_LIGHT
            FontDto.Weight.LIGHT -> FontWeight.LIGHT
            FontDto.Weight.MEDIUM -> FontWeight.MEDIUM
            FontDto.Weight.NORMAL -> FontWeight.NORMAL
            FontDto.Weight.SEMI_BOLD -> FontWeight.SEMI_BOLD
            FontDto.Weight.THIN -> FontWeight.THIN
        }
fun getFontPosture(font:FontDto):FontPosture =
        when (font.posture) {
            FontDto.Posture.ITALIC -> FontPosture.ITALIC
            FontDto.Posture.NORMAL -> FontPosture.REGULAR
        }

//Функция для определения стиля конца штриха в терминах JavaFX по описанию AWT
fun getStrokeLineCap(stroke:StrokeDto):StrokeLineCap =
        when (stroke.cap) {
            StrokeDto.CAP.ROUND -> StrokeLineCap.ROUND
            StrokeDto.CAP.SQUARE -> StrokeLineCap.SQUARE
            else -> StrokeLineCap.BUTT
        }

fun getStrokeLineJoin(stroke:StrokeDto):StrokeLineJoin =
        when (stroke.join) {
            StrokeDto.JOIN.BEVEL -> StrokeLineJoin.BEVEL
            StrokeDto.JOIN.MITER -> StrokeLineJoin.MITER
            else -> StrokeLineJoin.ROUND
        }

//Функция для преобразования в цвет JavaFX
fun ColorDto.transform(): Color = Color(this.r, this.g, this.b, this.a)

//Функция для преобразования цвета в DTO цвет
fun Color.transform():ColorDto = ColorDto(this.red, this.green, this.blue, this.opacity)

fun FontDto?.transform():Font =
        if (this == null) Font.getDefault()
        else Font.font(this.name, getFontWeight(this), getFontPosture(this), this.size)

fun Path.cubiccurveTo(controlX1:Double, controlY1:Double,
                      controlX2:Double, controlY2:Double,
                      endX:Double, endY:Double) {
    val cubiccurve = CubicCurveTo()
    cubiccurve.controlX1 = controlX1; cubiccurve.controlY1 = controlY1
    cubiccurve.controlX2 = controlX2; cubiccurve.controlY2 = controlY2
    cubiccurve.x = endX; cubiccurve.y = endY
    this.elements.add(cubiccurve)
}

//Метод для преобразования формы в форму JavaFX
fun ShapeDto.transform():Shape =
        when (this) {
            is ArcDto -> Arc(this.centerX, this.centerY, this.radiusX,
                    this.radiusY, this.startAngle, this.length)
            is CircleDto -> Circle(this.centerX, this.centerY, this.radius)
            is CubicCurveDto -> CubicCurve(
                    this.startX, this.startY, this.ctrlX1, this.ctrlY1,
                    this.ctrlX2, this.ctrlY2, this.endX, this.endY)
            is EllipseDto -> Ellipse(this.centerX, this.centerY, this.radiusX, this.radiusY)
            is LineDto -> Line(this.startX, this.startY, this.endX, this.endY)
            is PathDto -> {
                val customPath = Path()
                customPath.fillRule =
                        if(this.windingRule == PathDto.FILLRULES.EVEN_ODD) FillRule.EVEN_ODD
                        else FillRule.NON_ZERO

                this.segments.forEach {
                    when (it.action) {
                        SegmentDto.ACTIONS.MOVETO -> customPath.moveTo(it.points[0], it.points[1])
                        SegmentDto.ACTIONS.LINETO -> customPath.moveTo(it.points[0], it.points[1])
                        SegmentDto.ACTIONS.CUBICTO ->
                            customPath.cubiccurveTo(it.points[0], it.points[1], it.points[2], it.points[3],
                                    it.points[4], it.points[5])
                        SegmentDto.ACTIONS.QUADTO ->
                            customPath.quadqurveTo(it.points[0], it.points[1], it.points[2], it.points[3])
                        SegmentDto.ACTIONS.CLOSE ->
                            customPath.closepath()
                    }
                }
                customPath
            }
            is PolygonDto -> {
                val polygon = Polygon()
                for (i in this.xpoints.indices)
                    polygon.points.addAll(this.xpoints[i], this.ypoints[i])
                polygon
            }
            is QuadCurveDto -> QuadCurve(this.startX, this.startY, this.ctrlX, this.ctrlY, this.endX, this.endY)
            is RectangleDto -> {
                val rectangle = Rectangle(this.x, this.y, this.width, this.height)
                rectangle.arcHeight = this.arcHeight
                rectangle.arcWidth = this.arcWidth
                rectangle
            }
            else -> Circle()
        }