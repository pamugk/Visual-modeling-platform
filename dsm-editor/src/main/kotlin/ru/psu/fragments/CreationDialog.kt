package ru.psu.fragments

import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import ru.psu.controllers.MainController
import ru.psu.repository.entries.ModelEntry
import tornadofx.*

enum class CreationOutcome {
    NOTHING, MODEL, METAMODEL
}

class CreationDialog(): Fragment() {
    override val root: BorderPane by fxml()
    private val controller: MainController by inject()
    private val stepTitleLabel: Label by fxid()
    private val stepsPane:TabPane by fxid()
    private val typeTab:Tab by fxid()
    private val prototypeTab:Tab by fxid()
    private val prototypesList:ListView<ModelEntry> by fxid()
    private val descriptionTab:Tab by fxid()
    val nameField:TextField by fxid()
    val descriptionField:TextArea by fxid()

    private val prevBtn:Button by fxid()
    private val nextBtn:Button by fxid()

    var outcome:CreationOutcome = CreationOutcome.NOTHING
    var finishRestricted: Boolean = true

    init {
        nameField.textProperty().addListener {
            observable, old, new ->
            finishRestricted = new.isEmpty() || new.length > 100 || descriptionField.text.isEmpty()
            if (descriptionTab.isSelected())
                nextBtn.isDisable = finishRestricted
        }
        descriptionField.textProperty().addListener {
            observable, old, new ->
            finishRestricted = new.isEmpty() || nameField.text.isEmpty() || nameField.text.length > 100
            if (descriptionTab.isSelected())
                nextBtn.isDisable = finishRestricted
        }
    }

    fun cancel() {
        outcome = CreationOutcome.NOTHING
        this.currentStage?.close()
    }

    fun goPrev() {
        nextBtn.isDisable = false
        nextBtn.text = messages["btn.next"]
        when(stepsPane.selectionModel.selectedItem) {
            prototypeTab -> setTypeTab()
            descriptionTab -> if(outcome == CreationOutcome.METAMODEL) setTypeTab() else setPrototypeTab()
        }
    }

    fun goNext() {
        prevBtn.isDisable = false
        nextBtn.isDisable = true
        when(stepsPane.selectionModel.selectedItem) {
            typeTab -> if(outcome == CreationOutcome.METAMODEL) setDescriptionTab() else setPrototypeTab()
            prototypeTab -> setDescriptionTab()
            descriptionTab -> save()
        }
    }

    fun metamodelSelected() {
        outcome = CreationOutcome.METAMODEL
        nextBtn.isDisable = false
    }

    fun modelSelected() {
        outcome = CreationOutcome.MODEL
        nextBtn.isDisable = false
        nameField.clear()
        descriptionField.clear()
    }
    private fun save() {
        this.currentStage?.close()
    }

    private fun setDescriptionTab() {
        stepTitleLabel.text = messages["title.description"]
        nextBtn.text = messages["btn.finish"]
        nextBtn.isDisable = finishRestricted
        stepsPane.selectionModel.select(descriptionTab)
    }

    private fun setPrototypeTab() {
        stepTitleLabel.text = messages["title.prototype"]
        stepsPane.selectionModel.select(prototypeTab)
    }

    private fun setTypeTab() {
        prevBtn.isDisable = true
        stepTitleLabel.text = messages["title.type"]
        stepsPane.selectionModel.select(typeTab)
    }
}