package ru.psu.components.cells

import javafx.scene.control.ListCell
import ru.psu.repository.entries.ModelEntry

class ModelEntryCell: ListCell<ModelEntry>() {
    override fun updateItem(item: ModelEntry?, empty: Boolean) {
        super.updateItem(item, empty)
        text = if (empty || item == null) null else item.name
    }
}