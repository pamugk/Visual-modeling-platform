package ru.psu.factories

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import ru.psu.components.cells.RelationCell
import ru.psu.controllers.MainController
import ru.psu.relations.MLRelation

class RelationCellFactory(private val controller: MainController): Callback<ListView<MLRelation>, ListCell<MLRelation>> {
    override fun call(param: ListView<MLRelation>?): ListCell<MLRelation> {
        return RelationCell(controller)
    }
}