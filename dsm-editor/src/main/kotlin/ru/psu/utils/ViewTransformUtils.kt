package ru.psu.utils

import javafx.scene.paint.Color
import javafx.scene.shape.*
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import ru.psu.transformer.move
import ru.psu.view.ConstructView
import ru.psu.view.auxiliaries.ColorDto
import ru.psu.view.auxiliaries.FontDto
import ru.psu.view.auxiliaries.StrokeDto
import ru.psu.view.auxiliaries.shapes.*
import ru.psu.view.auxiliaries.shapes.path.PathDto
import ru.psu.view.auxiliaries.shapes.path.SegmentDto
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
fun getStrokeLineCap(stroke: StrokeDto):StrokeLineCap =
        when (stroke.cap) {
            StrokeDto.Cap.ROUND -> StrokeLineCap.ROUND
            StrokeDto.Cap.SQUARE -> StrokeLineCap.SQUARE
            else -> StrokeLineCap.BUTT
        }

fun getStrokeLineJoin(stroke: StrokeDto):StrokeLineJoin =
        when (stroke.join) {
            StrokeDto.Join.BEVEL -> StrokeLineJoin.BEVEL
            StrokeDto.Join.MITER -> StrokeLineJoin.MITER
            else -> StrokeLineJoin.ROUND
        }

//Функция для преобразования в цвет JavaFX
fun ColorDto.transform(): Color = Color(this.r, this.g, this.b, this.a)

//Функция для преобразования цвета в DTO цвет
fun Color.transform():ColorDto = ColorDto(this.red, this.green, this.blue, this.opacity)

fun FontDto?.transform():Font =
        if (this == null) Font.getDefault()
        else Font.font(this.name, getFontWeight(this), getFontPosture(this), this.size)

fun Path.cubicCurveTo(controlX1:Double, controlY1:Double,
                      controlX2:Double, controlY2:Double,
                      endX:Double, endY:Double) {
    val cubicCurve = CubicCurveTo()
    cubicCurve.controlX1 = controlX1; cubicCurve.controlY1 = controlY1
    cubicCurve.controlX2 = controlX2; cubicCurve.controlY2 = controlY2
    cubicCurve.x = endX; cubicCurve.y = endY
    this.elements.add(cubicCurve)
}

//Метод для преобразования формы в форму JavaFX
fun ShapeDto.transform():Shape =
        when (this) {
            is ArcDto -> Arc(center.x, center.y, radius.x, radius.y, startAngle, length)
            is CircleDto -> Circle(center.x, center.y, radius)
            is CubicCurveDto -> CubicCurve(start.x, start.y, ctrl1.x, ctrl1.y, ctrl2.x, ctrl2.y, end.x, end.y)
            is EllipseDto -> Ellipse(center.x, center.y, radius.x, radius.y)
            is LineDto -> Line(start.x, start.y, end.x, end.y)
            is PathDto -> {
                val customPath = Path()
                customPath.fillRule =
                        if(this.windingRule == PathDto.FILLRULES.EVEN_ODD) FillRule.EVEN_ODD
                        else FillRule.NON_ZERO

                this.segments.forEach {
                    when (it.action) {
                        SegmentDto.Actions.MOVE_TO -> customPath.moveTo(it.points[0].x, it.points[0].y)
                        SegmentDto.Actions.LINE_TO -> customPath.moveTo(it.points[0].x, it.points[0].y)
                        SegmentDto.Actions.CUBIC_TO ->
                            customPath.cubicCurveTo(it.points[0].x, it.points[0].y, it.points[1].x, it.points[1].y,
                                    it.points[2].x, it.points[2].y)
                        SegmentDto.Actions.QUAD_TO ->
                            customPath.quadqurveTo(it.points[0].x, it.points[0].y, it.points[1].x, it.points[1].y)
                        SegmentDto.Actions.CLOSE ->
                            customPath.closepath()
                    }
                }
                customPath
            }
            is PolygonDto -> {
                val polygon = Polygon()
                for (point in points) polygon.points.addAll(point.x, point.y)
                polygon
            }
            is QuadCurveDto -> QuadCurve(start.x, start.y, ctrl.x, ctrl.y, end.x, end.y)
            is RectangleDto -> {
                val rectangle = Rectangle(leftUpperCorner.x, leftUpperCorner.y, this.width, this.height)
                rectangle.arcHeight = this.arcHeight
                rectangle.arcWidth = this.arcWidth
                rectangle
            }
            else -> Circle()
        }