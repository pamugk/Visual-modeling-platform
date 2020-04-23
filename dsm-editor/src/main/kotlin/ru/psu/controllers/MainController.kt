package ru.psu.controllers

import javafx.geometry.Point2D
import ru.psu.DsmPlatform
import ru.psu.addConstructView
import ru.psu.createEntity
import ru.psu.constructs.MLConstruct
import ru.psu.createPort
import ru.psu.entities.MLEntity
import ru.psu.graphs.MLGraph
import ru.psu.model.Model
import ru.psu.ports.MLPort
import ru.psu.ports.MLPortKinds
import ru.psu.repository.entries.ModelEntry
import ru.psu.repository.entries.ViewEntry
import ru.psu.view.ConstructView
import ru.psu.view.View
import ru.psu.view.auxiliaries.shapes.PointDto
import tornadofx.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

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

//"Модель" в редакторе
class MainController: Controller() {
    //Информация о текущем состоянии "Модели"
    private enum class Models { NOTHING, METAMDEL, MODEL}
    private var curModel:Models = Models.NOTHING

    var prototype:Model? = null
        private set
    var currentPrototypeGraph: MLGraph? = null
        private set
    var currentModel: Model? = null
        private set
    var currentGraph: MLGraph? = null
        private set
    private var currentView:Int = 0
    private val prototypeViews:MutableList<View> = ArrayList()
    private val views:MutableList<View> = ArrayList()
    //

    //Информация о метаязыке и стандартном графическом представлении
    private var metalanguageModel: Model? = null
    private var metalanguageView: View? = null

    //Объект DSM-платформы
    var platform: DsmPlatform? = null
        set(value) {
            field = value
            if (value == null){
                metalanguageModel = null
                metalanguageView = null
            }
            else {
                metalanguageModel = value.getMetalanguageModel(messages)
                metalanguageView = value.getMetalanguageView(messages)
            }
    }

    //Метод для добавления сущности в модель
    //point - точка, где необходимо разместить представление модели
    fun addEntity(prototype:MLEntity, point:Point2D):UUID {
        val createdEntityId: UUID = currentModel!!.createEntity(currentGraph!!.id, prototype.name,
                prototype = if (curModel == Models.METAMDEL) null else prototype)
        val prototypeView:ConstructView = prototypeViews[currentView].constructViews[prototype.id]!!
        views[currentView].addConstructView(createdEntityId, prototypeView, shift = PointDto(point.x, point.y))
        return createdEntityId
    }

    //Метод для добавления порта
    fun addPort(entityId:UUID, prototype:MLPort, point:Point2D):UUID {
        val createdPortId: UUID = currentModel!!.createPort(entityId, prototype.name,
                kind = MLPortKinds.BIDIRECTIONAL, prototypeId = if (curModel == Models.METAMDEL) null else prototype.id)
        val prototypeView:ConstructView = prototypeViews[currentView].constructViews[prototype.id]!!
        views[currentView].addConstructView(createdPortId, prototypeView, shift = PointDto(point.x, point.y))
        return createdPortId
    }

    //Метод для закрытия модели (т.е. сброса состояния "Модели"
    fun closeModel() {
        curModel = Models.NOTHING
        prototype = null
        currentModel = null
        prototypeViews.clear()
        views.clear()
        currentPrototypeGraph = null
        currentGraph = null
        currentView = 0
    }

    //Метод для создания метамодели
    fun createMetamodel(name:String = "Metamodel", description:String = "") {
        prototype = metalanguageModel
        prototypeViews.add(metalanguageView!!)
        currentPrototypeGraph = prototype!!.graphs[prototype!!.root]
        currentModel = platform!!.createModel(null, name = name, description = description)
        currentGraph = currentModel!!.graphs[currentModel!!.root]
        views.add(platform!!.createView(currentModel!!, null, "", ""))
        curModel = Models.METAMDEL
    }

    //Метод для создания модели
    fun createModel(name:String = "Metamodel", description:String = "", reqPrototype: ModelEntry):CreationOutcome {
        prototype = platform!!.repository.loadModel(reqPrototype.id)
        if (prototype == null)
            return CreationOutcome.NO_PROTOTYPE
        currentPrototypeGraph = prototype!!.graphs[prototype!!.root]
        currentModel = platform!!.createModel(prototype!!, name = name, description = description)
        currentGraph = currentModel!!.graphs[currentModel!!.root]
        prototypeViews.addAll(platform!!.repository.loadModelViews(prototype!!.id))
        views.add(platform!!.createView(currentModel!!, prototypeViews[currentView].id, "", ""))
        curModel = Models.MODEL
        return CreationOutcome.SUCCESS
    }

    //TODO: метод для экспорта модели вовне системы
    fun export(destination: File){
        platform!!.repository.transferSystem.exporter.export(destination.path, currentModel!!)
    }

    //Метод для получения представления конструкции
    fun getConstructView(constructId:UUID):ConstructView? =
            if (views[currentView].constructViews.containsKey(constructId))
                views[currentView].constructViews[constructId]
            else null

    //Метод для получения представления конструкции из прототипа
    fun getPrototypeConstructView(prototype:MLConstruct):ConstructView? =
            if (prototypeViews[currentView].constructViews.containsKey(prototype.id))
                prototypeViews[currentView].constructViews[prototype.id]
            else null

    //TODO:Метод для импорта модели
    fun import(source: File){

    }

    //Метод для определения того, присутствует ли модель в данный момент
    fun isModelPresent():Boolean = currentModel != null

    //Метод для получения списка метамоделей из репозитория
    fun listMetamodels():List<ModelEntry> = platform!!.repository.listMetamodels()

    //Метод для получения списка моделей из репозитория
    fun listModels():List<ModelEntry> = platform!!.repository.listModels()

    //Метод для получения списка представлений модели из репозитория
    fun listViews():List<ViewEntry> = platform!!.repository.listViewsOfModel(currentModel!!)

    //Метод для открытия модели из репозитория
    fun openModel(selectedModel: ModelEntry){
        currentModel = platform!!.repository.loadModel(selectedModel.id)
        views.addAll(platform!!.repository.loadModelViews(selectedModel.id))
        currentGraph = currentModel!!.graphs[currentModel!!.root]
        if (selectedModel.prototypeId == null){
            prototype = metalanguageModel
            prototypeViews.add(metalanguageView!!)
        }
        else {
            prototype = platform!!.repository.loadModel(selectedModel.prototypeId!!)
            prototypeViews.addAll(platform!!.repository.loadModelViews(selectedModel.prototypeId!!))
        }
        currentPrototypeGraph = prototype!!.graphs[prototype!!.root]
    }

    //Метод для сохранения модели
    fun saveModel():SaveOutcome {
        if (currentModel == null)
            return SaveOutcome.NO_MODEL
        if (!platform!!.repository.saveModel(currentModel!!))
            return SaveOutcome.MODEL_SAVING_FAILURE
        return if (platform!!.repository.saveViews(views)) SaveOutcome.SUCCESS else SaveOutcome.VIEWS_SAVING_FAILURE
    }
}