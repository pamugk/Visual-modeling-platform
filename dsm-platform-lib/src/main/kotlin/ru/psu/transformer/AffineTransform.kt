package ru.psu.transformer

import ru.psu.view.auxiliaries.shapes.*

fun ShapeDto.basic():ShapeDto =
        when (this) {
            is ArcDto -> ArcDto(0.0, 0.0, radiusX, radiusY, startAngle, length)
            is CircleDto -> CircleDto(0.0, 0.0, radius)
            is CubicCurveDto -> CubicCurveDto(
                    startX , startY,
                    ctrlX1, ctrlY1,
                    ctrlX2, ctrlY2,
                    endX, endY)
            is EllipseDto -> EllipseDto(0.0, 0.0, 0.0, radiusY)
            is LineDto -> LineDto(startX, startY, endX, endY)
            is PathDto -> PathDto(windingRule, segments.map {
                SegmentDto(it.action, when (it.action) {
                    SegmentDto.ACTIONS.MOVETO -> doubleArrayOf(it.points[0], it.points[1])
                    SegmentDto.ACTIONS.LINETO -> doubleArrayOf(it.points[0], it.points[1])
                    SegmentDto.ACTIONS.CUBICTO -> doubleArrayOf(
                            it.points[0], it.points[1],
                            it.points[2], it.points[3],
                            it.points[4], it.points[5])
                    SegmentDto.ACTIONS.QUADTO -> doubleArrayOf(
                            it.points[0], it.points[1],
                            it.points[2], it.points[3])
                    SegmentDto.ACTIONS.CLOSE -> DoubleArray(0)
                })
            }.toMutableList())
            is PolygonDto -> PolygonDto(
                    xpoints.map { it }.toDoubleArray(),
                    ypoints.map { it }.toDoubleArray())
            is QuadCurveDto -> QuadCurveDto(
                    startX, startY,
                    ctrlX, ctrlY,
                    endX, endY)
            is RectangleDto -> RectangleDto(0.0, 0.0, width, height)
            else -> CircleDto(0.0, 0.0, 1.0)
        }

fun ShapeDto.move(shift: PointDto): ShapeDto =
        when (this) {
            is ArcDto -> ArcDto(centerX + shift.x, centerY + shift.y, radiusX,
                    radiusY, startAngle, length)
            is CircleDto -> CircleDto(centerX + shift.x, centerY + shift.y, radius)
            is CubicCurveDto -> CubicCurveDto(
                    startX + shift.x, startY + shift.y,
                    ctrlX1 + shift.x, ctrlY1 + shift.y,
                    ctrlX2 + shift.x, ctrlY2 + shift.y,
                    endX + shift.x, endY + shift.y)
            is EllipseDto -> EllipseDto(
                    centerX + shift.x, centerY + shift.y,
                    radiusX, radiusY)
            is LineDto -> LineDto(
                    startX + shift.x, startY + shift.y,
                    endX + shift.x, endY + shift.y)
            is PathDto -> PathDto(windingRule, segments.map {
                SegmentDto(it.action, when (it.action) {
                    SegmentDto.ACTIONS.MOVETO -> doubleArrayOf(it.points[0] + shift.x, it.points[1] + shift.y)
                    SegmentDto.ACTIONS.LINETO -> doubleArrayOf(it.points[0] + shift.x, it.points[1] + shift.y)
                    SegmentDto.ACTIONS.CUBICTO -> doubleArrayOf(
                            it.points[0] + shift.x, it.points[1] + shift.y,
                            it.points[2] + shift.x, it.points[3] + shift.y,
                            it.points[4] + shift.x, it.points[5] + shift.y)
                    SegmentDto.ACTIONS.QUADTO -> doubleArrayOf(
                            it.points[0] + shift.x, it.points[1] + shift.y,
                            it.points[2] + shift.x, it.points[3] + shift.y)
                    SegmentDto.ACTIONS.CLOSE -> DoubleArray(0)
                })
            }.toMutableList())
            is PolygonDto -> PolygonDto(
                    xpoints.map { it + shift.x }.toDoubleArray(),
                    ypoints.map { it + shift.y }.toDoubleArray())
            is QuadCurveDto -> QuadCurveDto(
                    startX + shift.x, startY + shift.y,
                    ctrlX + shift.x, ctrlY + shift.y,
                    endX + shift.x, endY + shift.y)
            is RectangleDto -> RectangleDto(x + shift.x, y + shift.y, width, height)
            else -> CircleDto(0.0, 0.0, 1.0)
        }