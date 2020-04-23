package ru.psu.factories

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import ru.psu.components.cells.ModelEntryCell
import ru.psu.repository.entries.ModelEntry

class ModelEntryCellFactory: Callback<ListView<ModelEntry>, ListCell<ModelEntry>> {
    override fun call(param: ListView<ModelEntry>?): ListCell<ModelEntry> {
        return ModelEntryCell()
    }
}