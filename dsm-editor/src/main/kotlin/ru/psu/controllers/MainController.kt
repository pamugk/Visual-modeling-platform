package ru.psu.controllers

import ru.psu.DsmPlatform
import ru.psu.model.Model
import ru.psu.repository.entries.ModelEntry
import ru.psu.repository.entries.ViewEntry
import tornadofx.*

class MainController: Controller() {
    var currentModel: Model? = null
        private set

    lateinit var platform: DsmPlatform

    fun createModel() {

    }

    fun createModel(prototype: ModelEntry) {

    }

    fun listMetamodels():List<ModelEntry> = platform.repository.listMetamodels()

    fun listModels():List<ModelEntry> = platform.repository.listModels()

    fun listViews():List<ViewEntry> = platform.repository.listViewsOfModel(currentModel!!)

    fun openModel(selectedModel: ModelEntry){
        currentModel = platform.repository.loadModel(selectedModel.id)
    }

    fun saveModel():Boolean = currentModel != null && platform.repository.saveModel(currentModel!!)

    fun closeModel() {

    }
}