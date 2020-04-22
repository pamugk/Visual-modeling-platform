package ru.psu.views

import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.stage.DirectoryChooser
import javafx.stage.Modality
import ru.psu.DsmPlatform
import ru.psu.controllers.MainController
import ru.psu.controllers.SaveOutcome
import ru.psu.entities.MLEntity
import ru.psu.factories.ConstructCellFactory
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
import java.io.File


class MainView : View() {
    override val root: BorderPane by fxml()
    private val controller: MainController by inject()

    private val directoryChooser:DirectoryChooser = DirectoryChooser()

    private val createBtn:MenuItem by fxid()
    private val openBtn:MenuItem by fxid()
    private val importBtn:MenuItem by fxid()
    private val saveBtn:MenuItem by fxid()
    private val exportBtn:MenuItem by fxid()
    private val closeBtn:MenuItem by fxid()
    private val constructsAccordion:Accordion by fxid()
    private val entityList:ListView<MLEntity> by fxid()
    private val relationList:ListView<MLRelation> by fxid()
    private val portList:ListView<MLPort> by fxid()

    private val modelPane:Pane by fxid()

    init {
        setStageIcon(Image(File("icons/app.png").toURI().toASCIIString()))
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
        entityList.cellFactory = ConstructCellFactory<MLEntity>(controller)
        entityList.selectionModel.selectedItemProperty().addListener {
            _, _, new: MLEntity? ->
            if (new != null) {
                relationList.selectionModel.clearSelection()
                portList.selectionModel.clearSelection()
            }
        }
        relationList.cellFactory = ConstructCellFactory<MLRelation>(controller)
        relationList.selectionModel.selectedItemProperty().addListener {
            _, _, new: MLRelation? ->
            if (new != null) {
                entityList.selectionModel.clearSelection()
                portList.selectionModel.clearSelection()
            }
        }
        portList.cellFactory = ConstructCellFactory<MLPort>(controller)
        portList.selectionModel.selectedItemProperty().addListener {
            _, _, new: MLPort? ->
            if (new != null) {
                entityList.selectionModel.clearSelection()
                relationList.selectionModel.clearSelection()
            }
        }
        currentStage?.minWidth = 640.0
        currentStage?.minHeight = 480.0
    }

    private fun <T> ListView<T>.clear() {
        this.selectionModel.clearSelection()
        this.items.clear()
    }

    private fun askAboutSave():Boolean {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = messages["save.title"]
        alert.dialogPane.minHeight = Region.USE_PREF_SIZE;
        alert.contentText = messages["save.text"]
        val yesButton = ButtonType(messages["dialog.yes"], ButtonBar.ButtonData.YES)
        val noButton = ButtonType(messages["dialog.no"], ButtonBar.ButtonData.NO)
        val cancelButton = ButtonType(messages["dialog.cancel"], ButtonBar.ButtonData.CANCEL_CLOSE)
        alert.buttonTypes.setAll(yesButton, noButton, cancelButton)
        val answer = alert.showAndWait()
        if (!answer.isPresent)
            return false
        if (answer.get().buttonData == ButtonBar.ButtonData.YES)
            controller.saveModel()
        return answer.get().buttonData == ButtonBar.ButtonData.CANCEL_CLOSE
    }

    private fun changeUiState(disableUi:Boolean) {
        val enableUi = !disableUi
        createBtn.isDisable = enableUi
        openBtn.isDisable = enableUi
        importBtn.isDisable = enableUi
        saveBtn.isDisable = disableUi
        exportBtn.isDisable = disableUi
        closeBtn.isDisable = disableUi
        constructsAccordion.isDisable = disableUi
    }

    private fun clearUi() {
        entityList.clear()
        relationList.clear()
        portList.clear()
        modelPane.clear()
    }

    fun closeModel() {
        if (controller.isModelPresent() && askAboutSave())
            return
        changeUiState(true)
        clearUi()
        controller.closeModel()
    }

    fun createModel() {
        val creationDialog = CreationDialog()
        creationDialog.openModal(modality = Modality.APPLICATION_MODAL, owner = this.currentWindow,
                block = true, resizable = false)?.showAndWait()
        when(creationDialog.outcome){
            CreationOutcome.NOTHING -> {
                println("NOTHING created")
                return
            }
            CreationOutcome.METAMODEL -> controller.createMetamodel(creationDialog.name, creationDialog.description)
            CreationOutcome.MODEL -> controller.createModel(creationDialog.name, creationDialog.description,
                        creationDialog.selectedPrototype!!)
        }
        updateAcessibleConstructs()
        changeUiState(false)
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
        val selectedDirectory = directoryChooser.showDialog(this.currentWindow) ?: return
        controller.import(selectedDirectory)
        changeUiState(false)
    }

    fun openModel() {
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

    fun showAbout() = showInfoDialog(Alert.AlertType.INFORMATION, messages["about.title"], messages["about.text"])

    fun showHelp() {

    }

    private fun showInfoDialog(type:Alert.AlertType, title:String, text:String) {
        val alert = Alert(type); alert.headerText = null; alert.graphic = null;
        alert.title = title; alert.contentText = text
        alert.dialogPane.minHeight = Region.USE_PREF_SIZE;
        alert.showAndWait()
    }

    private fun updateAcessibleConstructs() {
        val currentGraph = controller.currentPrototypeGraph!!
        currentGraph.entities.forEach {
            val entityPrototype:MLEntity = controller.prototype!!.constructs[it] as MLEntity
            if (entityPrototype.maxCount != 0)
                entityList.items.add(entityPrototype)
        }
        currentGraph.relations.forEach {
            val relationPrototype:MLRelation = controller.prototype!!.constructs[it] as MLRelation
            if (relationPrototype.maxCount != 0)
                relationList.items.add(relationPrototype)
        }
        currentGraph.ports.forEach {
            val portPrototype:MLPort = controller.prototype!!.constructs[it] as MLPort
            if (portPrototype.maxCount != 0)
                portList.items.add(portPrototype)
        }
    }
}