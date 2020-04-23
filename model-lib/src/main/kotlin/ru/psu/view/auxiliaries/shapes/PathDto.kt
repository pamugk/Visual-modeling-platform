package ru.psu.view.auxiliaries.shapes

class PathDto(var windingRule:FILLRULES, val segments:MutableList<SegmentDto>):ShapeDto(Shapes.Path) {
    enum class FILLRULES { EVEN_ODD, NON_ZERO }
}