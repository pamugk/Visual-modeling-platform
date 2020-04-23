package ru.psu.fragments

import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import ru.psu.controllers.MainController
import ru.psu.factories.ModelEntryCellFactory
import ru.psu.repository.entries.ModelEntry
import ru.psu.utils.clear
import tornadofx.*

class OpenDialog: Fragment() {
    override val root: BorderPane by fxml()
    private val controller: MainController by inject()
    private val modelList: ListView<ModelEntry> by fxid()
    private val idLabel: Label by fxid()
    private val nameLabel:Label by fxid()
    private val descriptionArea:TextArea by fxid()
    private val doneBtn:Button by fxid()

    var canceled:Boolean = true
        private set

    val selectedPrototype:ModelEntry?
        get() = modelList.selectedItem

    init {
        modelList.cellFactory = ModelEntryCellFactory()
        modelList.selectionModel.selectedItemProperty().addListener {
            _, _, new:ModelEntry? ->
            if (new == null)
                updateInfo("", "", "",  true)
            else
                updateInfo(new.id.toString(),  new.name,  new.description, false)
        }
    }

    fun cancel() {
        this.currentStage?.close()
    }

    fun done() {
        canceled = false
        this.currentStage?.close()
    }

    fun metamodelSelected() {
        modelList.clear()
        modelList.items.addAll(controller.listMetamodels())
    }

    fun modelSelected() {
        modelList.clear()
        modelList.items.addAll(controller.listModels())
    }

    private fun updateInfo(id:String, name:String, description:String,  disable:Boolean) {
        idLabel.text = id; nameLabel.text = name;
        descriptionArea.clear(); descriptionArea.text = description
        doneBtn.isDisable = disable
    }
}