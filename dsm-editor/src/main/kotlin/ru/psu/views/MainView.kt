package ru.psu.views

import javafx.scene.layout.BorderPane
import javafx.stage.Modality
import ru.psu.DsmPlatform
import ru.psu.controllers.MainController
import ru.psu.fragments.CreationDialog
import ru.psu.fragments.CreationOutcome
import ru.psu.generator.DslDefGenerator
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
    }

    fun createModel() {
        val creationDialog = CreationDialog()
        creationDialog.openModal(modality = Modality.APPLICATION_MODAL, owner = this.currentWindow,
                block = true, resizable = false)?.showAndWait()
        when(creationDialog.outcome){
            CreationOutcome.NOTHING -> println("NOTHING created")
            CreationOutcome.METAMODEL -> println("Metamodel created")
            CreationOutcome.MODEL -> println("Model created")
        }
    }

    fun export() {

    }

    fun import() {

    }

    fun openModel() {
    }

    fun saveModel() {
    }

    fun closeModel() {
    }

    fun exit() {
        this.currentStage?.close()
    }
}