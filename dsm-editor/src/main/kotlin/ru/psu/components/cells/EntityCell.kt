package ru.psu.components.cells

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.layout.StackPane
import ru.psu.controllers.MainController
import ru.psu.entities.MLEntity
import ru.psu.utils.constructShape
import ru.psu.utils.constructText
import ru.psu.view.ConstructView
import tornadofx.*
import java.io.IOException


class EntityCell(private val controller: MainController) : ListCell<MLEntity>() {
    init {
        try {
            val loader = FXMLLoader(javaClass.getResource("EntityCell.fxml"))
            loader.setRoot(this)
            loader.load<Any>()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @FXML
    private lateinit var viewPane: StackPane
    @FXML
    private lateinit var nameLabel: Label

    override fun updateItem(item: MLEntity?, empty: Boolean) {
        super.updateItem(item, empty)
        viewPane.clear()
        nameLabel.text = ""
        if (empty || item == null) {
            text = null
            contentDisplay = ContentDisplay.TEXT_ONLY
        }
        else {
            nameLabel.text = item.name
            val constructView:ConstructView? = controller.getPrototypeConstructView(item)
            if (constructView != null)
                drawView(constructView)
            contentDisplay = ContentDisplay.GRAPHIC_ONLY
        }
    }

    private fun drawView(constructView: ConstructView) {
        if (viewPane.shape == null)
            return
        viewPane.children.addAll(constructShape(constructView), constructText(constructView))
    }
}