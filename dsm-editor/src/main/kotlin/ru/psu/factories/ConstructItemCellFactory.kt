package ru.psu.factories

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import ru.psu.components.cells.ConstructItemCell
import ru.psu.constructs.MLConstruct
import ru.psu.controllers.MainController

class ConstructItemCellFactory<T:MLConstruct>(private val controller: MainController): Callback<ListView<T>, ListCell<T>> {
    override fun call(param: ListView<T>?): ListCell<T> {
        return ConstructItemCell(controller)
    }
}