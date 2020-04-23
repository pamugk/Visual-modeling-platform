package ru.psu.components.cells

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.layout.StackPane
import ru.psu.constructs.MLConstruct
import ru.psu.controllers.MainController
import ru.psu.utils.drawConstruct
import ru.psu.view.ConstructView
import tornadofx.*
import java.io.IOException

class ConstructItemCell<T: MLConstruct>(): ListCell<T>() {
    @FXML
    private lateinit var viewPane: StackPane
    @FXML
    private lateinit var nameLabel: Label

    private lateinit var controller: MainController

    constructor(controller: MainController):this() {
        this.controller = controller
        loadFxml()
    }

    private fun loadFxml() {
        try {
            val loader = FXMLLoader(javaClass.getResource("ConstructCell.fxml"))
            loader.setController(this)
            loader.setRoot(this)
            loader.load<Any>()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun updateItem(item: T?, empty: Boolean) {
        super.updateItem(item, empty)
        viewPane.clear()
        nameLabel.text = ""
        if (empty || item == null) {
            text = null
            contentDisplay = ContentDisplay.TEXT_ONLY
        }
        else {
            nameLabel.text = item.name
            val constructView: ConstructView? = controller.getPrototypeConstructView(item)
            if (constructView != null)
                viewPane.drawConstruct(constructView)
            contentDisplay = ContentDisplay.GRAPHIC_ONLY
        }
    }
}