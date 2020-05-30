package ru.psu.fragments

import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import ru.psu.controllers.MainController
import ru.psu.factories.ModelEntryCellFactory
import ru.psu.repository.entries.ModelEntry
import tornadofx.*

enum class CreationOutcome {
    NOTHING, MODEL, METAMODEL
}

class CreationDialog: Fragment() {
    override val root: BorderPane by fxml()
    private val controller: MainController by inject()
    private val stepTitleLabel: Label by fxid()
    private val stepsPane:TabPane by fxid()
    private val typeTab:Tab by fxid()
    private val prototypeTab:Tab by fxid()
    private val prototypesList:ListView<ModelEntry> by fxid()
    private val prototypeIdLabel:Label by fxid()
    private val prototypeNameLabel:Label by fxid()
    private val prototypeDescriptionArea:TextArea by fxid()
    private val descriptionTab:Tab by fxid()
    private val nameField:TextField by fxid()
    private val descriptionField:TextArea by fxid()

    val name:String
        get() = nameField.text
    val description:String
        get() = descriptionField.text
    val selectedPrototype:ModelEntry?
        get() = prototypesList.selectedItem

    private val prevBtn:Button by fxid()
    private val nextBtn:Button by fxid()

    var outcome:CreationOutcome = CreationOutcome.NOTHING
    private var finishRestricted: Boolean = true

    init {
        title = messages["title"]
        nameField.textProperty().addListener {
            _, _, new ->
            finishRestricted = new.isEmpty() || new.length > 100
            if (descriptionTab.isSelected)
                nextBtn.isDisable = finishRestricted
        }
        prototypesList.cellFactory = ModelEntryCellFactory()
        prototypesList.selectionModel.selectedItemProperty().addListener {
            _, _, new:ModelEntry? ->
            if (new == null) {
                prototypeIdLabel.text = ""
                prototypeNameLabel.text = ""
                prototypeDescriptionArea.clear()
                nextBtn.isDisable = true
            }
            else {
                prototypeIdLabel.text = new.id.toString()
                prototypeNameLabel.text = new.name
                prototypeDescriptionArea.text = new.description
                nextBtn.isDisable = false
            }
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
            descriptionTab -> this.currentStage?.close()
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

    private fun setDescriptionTab() {
        stepTitleLabel.text = messages["title.description"]
        nextBtn.text = messages["btn.finish"]
        nextBtn.isDisable = finishRestricted
        stepsPane.selectionModel.select(descriptionTab)
    }

    private fun setPrototypeTab() {
        stepTitleLabel.text = messages["title.prototype"]
        val selectedEntry = prototypesList.selectionModel.selectedItem
        prototypesList.items.clear()
        prototypesList.items.addAll(controller.listMetamodels())
        prototypesList.selectionModel.select(selectedEntry)
        stepsPane.selectionModel.select(prototypeTab)
    }

    private fun setTypeTab() {
        prevBtn.isDisable = true
        stepTitleLabel.text = messages["title.type"]
        stepsPane.selectionModel.select(typeTab)
    }
}