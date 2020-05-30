package ru.psu.transformer

import ru.psu.view.auxiliaries.shapes.*
import ru.psu.view.auxiliaries.shapes.path.PathDto
import ru.psu.view.auxiliaries.shapes.path.SegmentDto

//Метод для переноса фигуры к началу координат
//Применим только к фигурам, задаваемых одной точкой
fun ShapeDto.basic():ShapeDto =
        when (this) {
            is CircleDto -> CircleDto(PointDto(0.0, 0.0), radius)
            is EllipseDto -> EllipseDto(PointDto(0.0, 0.0), radius.copy())
            is RectangleDto -> RectangleDto(PointDto(), width, height)
            else -> this
        }

//Метод для переноса фигуры на некоторое смещение, задаваемое вектором shift
//Состоит в переносе всех точек, задающих фигуру
fun ShapeDto.move(shift: PointDto): ShapeDto =
        when (this) {
            is ArcDto -> ArcDto(center + shift, radius + shift, startAngle, length)
            is CircleDto -> CircleDto(center + shift, radius)
            is CubicCurveDto ->
                CubicCurveDto(start + shift, ctrl1 + shift, ctrl2 + shift, end + shift)
            is EllipseDto -> EllipseDto(center + shift, radius.copy())
            is LineDto -> LineDto(start + shift, end + shift)
            is PathDto -> PathDto(windingRule, segments.map {
                SegmentDto(it.action, when (it.action) {
                    SegmentDto.Actions.MOVE_TO -> listOf(it.points[0] + shift)
                    SegmentDto.Actions.LINE_TO -> listOf(it.points[0] + shift)
                    SegmentDto.Actions.CUBIC_TO -> listOf(it.points[0] + shift, it.points[1] + shift, it.points[2] + shift)
                    SegmentDto.Actions.QUAD_TO -> listOf(it.points[0] + shift, it.points[1] + shift)
                    SegmentDto.Actions.CLOSE -> listOf(PointDto())
                })
            }.toMutableList())
            is PolygonDto -> PolygonDto(points.map { it + shift }.toMutableList())
            is QuadCurveDto -> QuadCurveDto(start + shift, ctrl + shift, end + shift)
            is RectangleDto -> RectangleDto(leftUpperCorner + shift, width, height)
            else -> CircleDto(PointDto(), 1.0)
        }