package ru.psu.factories

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import ru.psu.components.cells.PortCell
import ru.psu.controllers.MainController
import ru.psu.ports.MLPort

class PortCellFactory(private val controller: MainController): Callback<ListView<MLPort>, ListCell<MLPort>> {
    override fun call(param: ListView<MLPort>?): ListCell<MLPort> {
        return PortCell(controller)
    }
}