package ru.psu.utils

import javafx.scene.control.ListView
import javafx.scene.layout.Pane
import javafx.scene.shape.Circle
import javafx.scene.shape.Ellipse
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import ru.psu.view.ConstructView
import ru.psu.view.auxiliaries.shapes.CircleDto
import ru.psu.view.auxiliaries.shapes.EllipseDto
import ru.psu.view.auxiliaries.shapes.RectangleDto
import ru.psu.view.auxiliaries.shapes.ShapeDto

fun <T> ListView<T>.clear() {
    this.selectionModel.clearSelection()
    this.items.clear()
}

fun Pane.drawConstruct(constructView: ConstructView) {
    if (constructView.shape == null)
        return
    this.children.addAll(constructShape(constructView), constructText(constructView))
}