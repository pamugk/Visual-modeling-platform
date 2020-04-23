package ru.psu.utils

import javafx.scene.control.ListView
import javafx.scene.layout.Pane
import ru.psu.view.ConstructView

fun <T> ListView<T>.clear() {
    this.selectionModel.clearSelection()
    this.items.clear()
}

fun Pane.drawConstruct(constructView: ConstructView) {
    if (constructView.shape == null)
        return
    this.children.addAll(constructShape(constructView), constructText(constructView))
}