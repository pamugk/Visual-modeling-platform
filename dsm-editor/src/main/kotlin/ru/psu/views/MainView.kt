package ru.psu.views

import ru.psu.Styles
import javafx.scene.control.Alert.AlertType.INFORMATION
import javafx.scene.layout.BorderPane
import ru.psu.controllers.MainController
import tornadofx.*

class MainView : View() {
    override val root: BorderPane by fxml()
    val controller: MainController by inject()

    fun createModel() {
        controller.createModel()
    }

    fun openModel() {
        controller.openModel()
    }

    fun saveModel() {
        controller.saveModel()
    }

    fun saveModelAs() {
        controller.saveModelAs()
    }

    fun closeModel() {
        controller.closeModel()
    }

    fun exit() {

    }
}