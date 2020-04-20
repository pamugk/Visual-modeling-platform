package ru.psu.views

import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.stage.DirectoryChooser
import javafx.stage.Modality
import ru.psu.DsmPlatform
import ru.psu.controllers.MainController
import ru.psu.controllers.SaveOutcome
import ru.psu.entities.MLEntity
import ru.psu.factories.EntityCellFactory
import ru.psu.factories.PortCellFactory
import ru.psu.factories.RelationCellFactory
import ru.psu.fragments.CreationDialog
import ru.psu.fragments.CreationOutcome
import ru.psu.generator.DslDefGenerator
import ru.psu.ports.MLPort
import ru.psu.relations.MLRelation
import ru.psu.repository.ModelRepository
import ru.psu.repository.ModelTransferSystem
import ru.psu.repository.transferImplementations.xml.XmlModelExporter
import ru.psu.repository.transferImplementations.xml.XmlModelImporter
import ru.psu.transformer.ModelTransformer
import ru.psu.validator.ModelValidator
import tornadofx.*


class MainView : View() {
    override val root: BorderPane by fxml()
    private val controller: MainController by inject()

    private val directoryChooser:DirectoryChooser = DirectoryChooser()

    private val entityList:ListView<MLEntity> by fxid()
    private val relationList:ListView<MLRelation> by fxid()
    private val portList:ListView<MLPort> by fxid()

    private val modelPane:Pane by fxid()

    init {
        title = messages["title"]
        controller.platform = DsmPlatform(
                DslDefGenerator(),
                ModelValidator(),
                ModelTransformer(),
                ModelRepository(
                        "./repository",
                        ModelTransferSystem(XmlModelExporter(), XmlModelImporter())
                )
        )
        entityList.cellFactory = EntityCellFactory(controller)
        relationList.cellFactory = RelationCellFactory(controller)
        portList.cellFactory = PortCellFactory(controller)
    }

    private fun askAboutSave():Boolean {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = messages["save.title"]
        alert.contentText = messages["save.text"]
        val yesButton = ButtonType(messages["dialog.yes"], ButtonBar.ButtonData.YES)
        val noButton = ButtonType(messages["dialog.no"], ButtonBar.ButtonData.NO)
        val cancelButton = ButtonType(messages["dialog.cancel"], ButtonBar.ButtonData.CANCEL_CLOSE)
        alert.buttonTypes.setAll(yesButton, noButton, cancelButton)
        val answer = alert.showAndWait()
        if (!answer.isPresent)
            return false
        if (answer.get() == ButtonType.YES)
            controller.saveModel()
        return answer.get() == ButtonType.CANCEL
    }

    private fun changeUiState(disableUi:Boolean) {

    }

    private fun clearupUi() {
        entityList.selectionModel.clearSelection()
        entityList.items.clear()
        relationList.selectionModel.clearSelection()
        relationList.items.clear()
        portList.selectionModel.clearSelection()
        portList.items.clear()
        modelPane.clear()
    }

    fun closeModel() {
        if (controller.isModelPresent() && askAboutSave())
            return
        changeUiState(true)
        clearupUi()
        controller.closeModel()
    }

    fun createModel() {
        if (controller.isModelPresent() && askAboutSave())
            return
        val creationDialog = CreationDialog()
        creationDialog.openModal(modality = Modality.APPLICATION_MODAL, owner = this.currentWindow,
                block = true, resizable = false)?.showAndWait()
        when(creationDialog.outcome){
            CreationOutcome.NOTHING -> println("NOTHING created")
            CreationOutcome.METAMODEL -> setupMetamodelUi()
            CreationOutcome.MODEL -> setupModelUi()
        }
    }

    fun exit() {
        if (controller.isModelPresent() && askAboutSave())
            return
        this.currentStage?.close()
    }

    fun export() {
        val selectedDirectory = directoryChooser.showDialog(this.currentWindow)
        if (selectedDirectory != null)
            controller.export(selectedDirectory)
    }

    fun import() {
        val selectedDirectory = directoryChooser.showDialog(this.currentWindow)
        if (selectedDirectory == null || controller.isModelPresent() && askAboutSave())
            return
        controller.closeModel()
        controller.import(selectedDirectory)
        changeUiState(false)
    }

    fun openModel() {
        if (controller.isModelPresent() && askAboutSave())
            return
        controller.closeModel()
    }

    fun saveModel() {
        when (controller.saveModel()) {
            SaveOutcome.NO_MODEL -> showInfoDialog(Alert.AlertType.ERROR,
                    messages["save.results.error.title"], messages["save.results.no_model.text"])
            SaveOutcome.MODEL_SAVING_FAILURE -> showInfoDialog(Alert.AlertType.ERROR,
                    messages["save.results.error.title"], messages["save.results.model_fail.text"])
            SaveOutcome.VIEWS_SAVING_FAILURE -> showInfoDialog(Alert.AlertType.ERROR,
                    messages["save.results.error.title"], messages["save.results.view_fail.text"])
            SaveOutcome.SUCCESS -> showInfoDialog(Alert.AlertType.INFORMATION,
                    messages["save.results.success.title"], messages["save.results.success.text"])
        }
    }

    private fun setupModelUi() {

    }

    private fun setupMetamodelUi() {

    }

    private fun showInfoDialog(type:Alert.AlertType, title:String, text:String) {
        val alert = Alert(type)
        alert.title = title
        alert.contentText = text
        alert.showAndWait()
    }
}