package ru.psu.view.auxiliaries.shapes.path

import ru.psu.view.auxiliaries.shapes.PointDto

class SegmentDto(var action: Actions, var points:List<PointDto>) {
    enum class Actions { MOVE_TO, LINE_TO, CUBIC_TO, QUAD_TO, CLOSE }
}