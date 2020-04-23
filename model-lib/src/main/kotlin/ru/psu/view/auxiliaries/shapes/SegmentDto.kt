package ru.psu.view.auxiliaries.shapes

class SegmentDto(var action:ACTIONS, var points:DoubleArray) {
    enum class ACTIONS { MOVETO, LINETO, CUBICTO, QUADTO, CLOSE }
}