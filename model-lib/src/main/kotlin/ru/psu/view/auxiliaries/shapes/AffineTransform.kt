package ru.psu.view.auxiliaries.shapes

fun ShapeDto.move(shift:PointDto):ShapeDto =
        when (this) {
            is ArcDto -> ArcDto(this.centerX + shift.x, this.centerY + shift.y, this.radiusX,
                    this.radiusY, this.startAngle, this.length)
            is CircleDto -> CircleDto(this.centerX + shift.x, this.centerY + shift.y, this.radius)
            is CubicCurveDto -> CubicCurveDto(
                    this.startX + shift.x, this.startY + shift.y,
                    this.ctrlX1 + shift.x, this.ctrlY1 + shift.y,
                    this.ctrlX2 + shift.x, this.ctrlY2 + shift.y,
                    this.endX + shift.x, this.endY + shift.y)
            is EllipseDto -> EllipseDto(
                    this.centerX + shift.x, this.centerY + shift.y,
                    this.radiusX, this.radiusY)
            is LineDto -> LineDto(
                    this.startX + shift.x, this.startY + shift.y,
                    this.endX + shift.x, this.endY + shift.y)
            is PathDto -> PathDto(this.windingRule, this.segments.map {
                        SegmentDto(it.action, when (it.action) {
                            SegmentDto.ACTIONS.MOVETO -> doubleArrayOf(it.points[0] + shift.x, it.points[1] + shift.y)
                            SegmentDto.ACTIONS.LINETO -> doubleArrayOf(it.points[0] + shift.x, it.points[1] + shift.y)
                            SegmentDto.ACTIONS.CUBICTO ->doubleArrayOf(
                                    it.points[0] + shift.x, it.points[1] + shift.y,
                                    it.points[2] + shift.x, it.points[3] + shift.y,
                                    it.points[4] + shift.x, it.points[5] + shift.y)
                            SegmentDto.ACTIONS.QUADTO -> doubleArrayOf(
                                    it.points[0] + shift.x, it.points[1] + shift.y,
                                    it.points[2] + shift.x, it.points[3] + shift.y)
                            SegmentDto.ACTIONS.CLOSE -> DoubleArray(0)
                        })}.toMutableList())
            is PolygonDto -> PolygonDto(
                        this.xpoints.map { it + shift.x }.toDoubleArray(),
                        this.ypoints.map { it + shift.y }.toDoubleArray())
            is QuadCurveDto -> QuadCurveDto(
                    this.startX + shift.x, this.startY + shift.y,
                    this.ctrlX + shift.x, this.ctrlY + shift.y,
                    this.endX + shift.x, this.endY + shift.y)
            is RectangleDto -> RectangleDto(this.x + shift.x, this.y + shift.y, this.width, this.height)
            else -> CircleDto(0.0,0.0,1.0)
        }