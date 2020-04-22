package ru.psu.utils

import javafx.scene.layout.Pane
import ru.psu.view.ConstructView

fun Pane.drawConstruct(constructView: ConstructView) {
    if (constructView.shape == null)
        return
    this.children.addAll(constructShape(constructView), constructText(constructView))
}