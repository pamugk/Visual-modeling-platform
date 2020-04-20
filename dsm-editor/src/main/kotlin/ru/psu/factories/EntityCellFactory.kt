package ru.psu.factories

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import ru.psu.components.cells.EntityCell
import ru.psu.controllers.MainController
import ru.psu.entities.MLEntity

class EntityCellFactory(private val controller:MainController): Callback<ListView<MLEntity>, ListCell<MLEntity>> {
    override fun call(param: ListView<MLEntity>?): ListCell<MLEntity> {
        return EntityCell(controller)
    }
}