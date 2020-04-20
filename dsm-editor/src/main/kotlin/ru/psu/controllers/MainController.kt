package ru.psu.controllers

import ru.psu.DsmPlatform
import ru.psu.constructs.MLConstruct
import ru.psu.model.Model
import ru.psu.repository.entries.ModelEntry
import ru.psu.repository.entries.ViewEntry
import ru.psu.view.ConstructView
import ru.psu.view.View
import tornadofx.*
import java.io.File

enum class CreationOutcome {
    NO_PROTOTYPE,
    SUCCESS
}

enum class SaveOutcome {
    NO_MODEL,
    MODEL_SAVING_FAILURE,
    VIEWS_SAVING_FAILURE,
    SUCCESS
}

class MainController: Controller() {
    var prototype:Model? = null
        private set
    var currentModel: Model? = null
        private set
    private var currentView:Int = 0
    val prototypeViews:MutableList<View> = ArrayList()
    val views:MutableList<View> = ArrayList()
    fun isModelPresent():Boolean = currentModel != null

    lateinit var platform: DsmPlatform

    fun closeModel() {
        prototype = null
        currentModel = null
        prototypeViews.clear()
        views.clear()
    }

    fun createMetamodel(name:String = "Metamodel", description:String = "") {
        currentModel = platform.createMetamodel(name, description)
        views.add(platform.createView(currentModel!!, null, "", ""))
    }

    fun createModel(name:String = "Metamodel", description:String = "", reqPrototype: ModelEntry):CreationOutcome {
        prototype = platform.repository.loadModel(reqPrototype.id)
        if (prototype == null)
            return CreationOutcome.NO_PROTOTYPE
        currentModel = platform.createModel(prototype!!, name, description)
        return CreationOutcome.SUCCESS
    }

    fun export(destination: File){
        platform.repository.transferSystem.exporter.export(destination.path, currentModel!!);
    }

    fun getPrototypeConstructView(prototype:MLConstruct):ConstructView? =
            if (prototypeViews[currentView].constructViews.containsKey(prototype.id))
                prototypeViews[currentView].constructViews[prototype.id]
            else null

    fun getConstructView(construct:MLConstruct):ConstructView? =
            if (views[currentView].constructViews.containsKey(construct.id))
                views[currentView].constructViews[construct.id]
            else null

    fun import(source: File){

    }

    fun listMetamodels():List<ModelEntry> = platform.repository.listMetamodels()

    fun listModels():List<ModelEntry> = platform.repository.listModels()

    fun listViews():List<ViewEntry> = platform.repository.listViewsOfModel(currentModel!!)

    fun openModel(selectedModel: ModelEntry){
        currentModel = platform.repository.loadModel(selectedModel.id)
        if (selectedModel.prototypeId != null)
            prototype = platform.repository.loadModel(selectedModel.prototypeId!!)
    }

    fun saveModel():SaveOutcome {
        if (currentModel == null)
            return SaveOutcome.NO_MODEL
        if (!platform.repository.saveModel(currentModel!!))
            return SaveOutcome.MODEL_SAVING_FAILURE
        return if (platform.repository.saveViews(views)) SaveOutcome.SUCCESS else SaveOutcome.VIEWS_SAVING_FAILURE
    }
}