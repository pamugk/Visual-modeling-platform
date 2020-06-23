package ru.psu.views

import javafx.application.Platform
import javafx.geometry.Point2D
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.shape.Shape
import javafx.scene.text.Text
import javafx.stage.DirectoryChooser
import javafx.stage.Modality
import ru.psu.DsmPlatform
import ru.psu.constructs.MLConstruct
import ru.psu.controllers.MainController
import ru.psu.controllers.SaveOutcome
import ru.psu.entities.MLEntity
import ru.psu.factories.ConstructItemCellFactory
import ru.psu.fragments.CreationDialog
import ru.psu.fragments.CreationOutcome
import ru.psu.fragments.OpenDialog
import ru.psu.fragments.ShapeDialog
import ru.psu.generator.DslDefGenerator
import ru.psu.graphs.MLGraph
import ru.psu.ports.MLPort
import ru.psu.relations.MLRelation
import ru.psu.repository.ModelRepository
import ru.psu.repository.ModelTransferSystem
import ru.psu.repository.transferImplementations.xml.XmlModelExporter
import ru.psu.repository.transferImplementations.xml.XmlModelImporter
import ru.psu.transformer.ModelTransformer
import ru.psu.transformer.move
import ru.psu.utils.clear
import ru.psu.utils.constructShape
import ru.psu.utils.constructText
import ru.psu.utils.transform
import ru.psu.validator.ModelValidator
import ru.psu.view.ConstructView
import ru.psu.view.auxiliaries.shapes.ShapeDto
import tornadofx.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

//Контроллер редактора
class MainView : View() {
    private enum class SelectedConstruct { NOTHING, ENTITY, RELATION, PORT }

    //Вспомогательная информация о текущем состоянии редактора:
    //тип текущей выбранной конструкции из палитры,
    //текущиая выбранная конструкция на полотне (тип, id и соответствующее ей отображение)
    private var listConstruct:SelectedConstruct = SelectedConstruct.NOTHING
    private var paneConstruct: SelectedConstruct = SelectedConstruct.NOTHING
    private var paneConstructId:UUID? = null
    private val paneConstructShapes:MutableMap<UUID, Shape> = mutableMapOf()
    private val paneConstructTexts:MutableMap<UUID, Text> = mutableMapOf()
    private var paneConstructView:ConstructView? = null

    private val clickedPorts:MutableList<UUID> = mutableListOf()

    //Список графических элементов окна,
    //с которыми есть необходимость взаимодействовать напрямую
    override val root: BorderPane by fxml()
    private val controller: MainController by inject()

    private val directoryChooser:DirectoryChooser = DirectoryChooser()

    private val createBtn:MenuItem by fxid()
    private val openBtn:MenuItem by fxid()
    private val importBtn:MenuItem by fxid()
    private val saveBtn:MenuItem by fxid()
    private val exportBtn:MenuItem by fxid()
    private val closeBtn:MenuItem by fxid()

    private val prototypeAccordion:Accordion by fxid()
    private val entityList:ListView<MLEntity> by fxid()
    private val relationList:ListView<MLRelation> by fxid()
    private val portList:ListView<MLPort> by fxid()

    private val mainScrollPane:ScrollPane by fxid()
    private val modelPane:Pane by fxid()

    private val constructAccordion:Accordion by fxid()
    private val constructIdLabel:Label by fxid()
    private val constructNameField:TextField by fxid()

    private val constructBackColorPicker:ColorPicker by fxid()
    private val constructContent:TextField by fxid()
    private val constructFont:Label by fxid()
    private val constructStrokeColorPicker:ColorPicker by fxid()
    //

    //Дополнительная инициализация:
    //создание экземпляра  платформы,
    //добавление обработчиков событий,
    //дополнение определения окна (иконка, заголовок, ограничения на размеры)
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
        entityList.cellFactory = ConstructItemCellFactory<MLEntity>(controller)
        entityList.selectionModel.selectedItemProperty().addListener {
            _, _, new: MLEntity? ->
            if (new != null) {
                listConstruct = SelectedConstruct.ENTITY
                relationList.selectionModel.clearSelection()
                portList.selectionModel.clearSelection()
            }
        }
        relationList.cellFactory = ConstructItemCellFactory<MLRelation>(controller)
        relationList.selectionModel.selectedItemProperty().addListener {
            _, _, new: MLRelation? ->
            if (new != null) {
                listConstruct = SelectedConstruct.RELATION
                entityList.selectionModel.clearSelection()
                portList.selectionModel.clearSelection()
            }
        }
        portList.cellFactory = ConstructItemCellFactory<MLPort>(controller)
        portList.selectionModel.selectedItemProperty().addListener {
            _, _, new: MLPort? ->
            if (new != null) {
                listConstruct = SelectedConstruct.PORT
                entityList.selectionModel.clearSelection()
                relationList.selectionModel.clearSelection()
            }
        }
        constructNameField.textProperty().addListener{
            _, old, new ->
            if (paneConstruct == SelectedConstruct.NOTHING) return@addListener
            val construct = controller.currentModel?.constructs?.get(paneConstructId!!)
            if (new == null || new.isEmpty())
                constructNameField.text = old
            else {
                construct!!.name = new
                if (construct is MLEntity) {
                    val view:ConstructView = controller.getConstructView(construct.id)!!
                    view.content = new
                    if (paneConstructTexts.containsKey(construct.id))
                        modelPane.children.remove(paneConstructTexts[construct.id])
                    val newText = constructText(view)
                    paneConstructTexts[construct.id] = newText
                    modelPane.children.add(newText)
                }
            }
        }
        constructBackColorPicker.valueProperty().addListener {
            _, oldColor, newColor ->
            if (paneConstruct == SelectedConstruct.NOTHING) return@addListener
            val view = controller.getConstructView(paneConstructId!!) ?: return@addListener
            if (newColor == null)
                constructBackColorPicker.value = oldColor
            else {
                view.backColor = newColor.transform()
                paneConstructShapes[paneConstructId!!]!!.fill = newColor
            }
        }
        constructStrokeColorPicker.valueProperty().addListener {
            _, oldColor, newColor ->
            if (paneConstruct == SelectedConstruct.NOTHING) return@addListener
            val view = controller.getConstructView(paneConstructId!!) ?: return@addListener
            if (newColor == null)
                constructStrokeColorPicker.value = oldColor
            else {
                view.strokeColor = newColor.transform()
                paneConstructShapes[paneConstructId!!]!!.stroke = newColor
            }
        }
        currentStage?.minWidth = 640.0
        currentStage?.minHeight = 480.0
    }

    //Метод для добавления отображения сущности на полотно
    private fun addEntity(id:UUID) {
        val view = controller.getConstructView(id)!!
        val shape:Shape = constructShape(view)
        val text: Text = constructText(view)
        shape.setOnMouseClicked { mouseEvent: MouseEvent ->
            clickedPorts.clear()
            if (mouseEvent.button != MouseButton.PRIMARY)
                return@setOnMouseClicked
            Platform.runLater {
                selectConstruct(SelectedConstruct.NOTHING)
                initializeConstructInfo()
                selectConstruct(SelectedConstruct.ENTITY, id, shape)
                initializeConstructInfo()
            }
            if (listConstruct == SelectedConstruct.PORT) {
                val portId: UUID = controller.addPort(id, portList.selectedItem!!, Point2D(mouseEvent.x, mouseEvent.y))
                portList.selectionModel.clearSelection()
                listConstruct = SelectedConstruct.NOTHING
                addPort(portId)
            }
            mouseEvent.consume()
        }
        modelPane.children.addAll(shape, text)
        paneConstructShapes[id] = shape
        paneConstructTexts[id] = text
    }

    //Метод для добавления отображения порта на полотно
    private fun addPort(id:UUID) {
        val shape:Shape = constructShape(controller.getConstructView(id)!!)
        shape.setOnMouseClicked { mouseEvent: MouseEvent ->
            if (mouseEvent.button != MouseButton.PRIMARY) {
                return@setOnMouseClicked
            }
            Platform.runLater {
                selectConstruct(SelectedConstruct.NOTHING)
                initializeConstructInfo()
                selectConstruct(SelectedConstruct.PORT, id, shape)
                initializeConstructInfo()
            }
            if (listConstruct == SelectedConstruct.RELATION) {
                if (!clickedPorts.contains(id)) clickedPorts.add(id)
                if (clickedPorts.size == 2) {
                    val relId: UUID = controller.addRelation(relationList.selectedItem!!, clickedPorts)
                    relationList.selectionModel.clearSelection()
                    listConstruct = SelectedConstruct.NOTHING
                    clickedPorts.clear()
                    addRelation(relId)
                }
            }
            mouseEvent.consume()
        }
        modelPane.children.add(shape)
        paneConstructShapes[id] = shape
    }

    //Метод для отображения отношения на полотно
    private fun addRelation(id:UUID) {
        val shape:Shape = constructShape(controller.getConstructView(id)!!)
        shape.setOnMouseClicked { mouseEvent: MouseEvent ->
            clickedPorts.clear()
            if (mouseEvent.button != MouseButton.PRIMARY)
                return@setOnMouseClicked
            Platform.runLater {
                selectConstruct(SelectedConstruct.NOTHING)
                initializeConstructInfo()
                selectConstruct(SelectedConstruct.RELATION, id, shape)
                initializeConstructInfo()
            }
            mouseEvent.consume()
        }
        modelPane.children.add(shape)
        paneConstructShapes[id] = shape
    }

    //Метод для отображения диалога касательно того, необходимо ли сохранять изменения в модели
    private fun askAboutSave():Boolean {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = messages["save.title"]
        alert.dialogPane.minHeight = Region.USE_PREF_SIZE
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

    //Обработчик события нажатия мыши на полотно
    fun canvasClicked(event:MouseEvent) {
        clickedPorts.clear()
        if (event.button != MouseButton.PRIMARY || listConstruct != SelectedConstruct.ENTITY) {
            selectConstruct(SelectedConstruct.NOTHING)
            initializeConstructInfo()
            return
        }
        val id: UUID = controller.addEntity(entityList.selectedItem!!, Point2D(event.x, event.y))
        entityList.selectionModel.clearSelection()
        listConstruct = SelectedConstruct.NOTHING
        addEntity(id)
    }

    fun changeShape() {
        val dialog = ShapeDialog()
        dialog.updateShapeProperties(paneConstructView!!.shape!!)
        dialog.openModal(modality = Modality.APPLICATION_MODAL, owner = this.currentWindow,
                block = true, resizable = false)?.showAndWait()
        if (dialog.save) {
            paneConstructView!!.shape = dialog.shape!!.move(paneConstructView!!.position)
            modelPane.children.remove(paneConstructShapes[paneConstructId])
            addEntity(paneConstructId!!)
        }
    }

    //Метод для изменения активности интерфейса
    private fun changeUiState(disableUi:Boolean) {
        val enableUi = !disableUi
        createBtn.isDisable = enableUi
        openBtn.isDisable = enableUi
        importBtn.isDisable = enableUi
        saveBtn.isDisable = disableUi
        exportBtn.isDisable = disableUi
        closeBtn.isDisable = disableUi
        prototypeAccordion.isDisable = disableUi
        mainScrollPane.isDisable = disableUi
    }

    //Метод для очистки интерфейса
    private fun clearUi() {
        entityList.clear()
        relationList.clear()
        portList.clear()
        modelPane.clear()
        prototypeAccordion.expandedPane?.isExpanded = false
        mainScrollPane.hvalue = mainScrollPane.hmin
        mainScrollPane.vvalue = mainScrollPane.vmin
        selectConstruct(SelectedConstruct.NOTHING)
        initializeConstructInfo()
    }

    //Обработчик нажатия на кнопку "Закрыть модель"
    fun closeModel() {
        if (controller.isModelPresent() && askAboutSave())
            return
        listConstruct = SelectedConstruct.NOTHING
        changeUiState(true)
        clearUi()
        controller.closeModel()
        title = messages["title"]
    }

    //Обработчик нажатия на кнопку "Создать модель"
    fun createModel() {
        //Отображаем диалог на создание модели
        val creationDialog = CreationDialog()
        creationDialog.openModal(modality = Modality.APPLICATION_MODAL, owner = this.currentWindow,
                block = true, resizable = false)?.showAndWait()
        //Определяем выбор пользователя
        when(creationDialog.outcome){
            //Ничего создавать не надо
            CreationOutcome.NOTHING -> {
                println("NOTHING created")
                return
            }
            //Создаётся метамодель
            CreationOutcome.METAMODEL -> controller.createMetamodel(creationDialog.name, creationDialog.description)
            //Создаётся модель
            CreationOutcome.MODEL -> controller.createModel(creationDialog.name, creationDialog.description,
                        creationDialog.selectedPrototype!!)
        }
        title = "${messages["title"]}: ${creationDialog.name}"
        updateAccessibleConstructs()
        changeUiState(false)
    }

    //Обработчик нажатия на кнопку "Выход"
    fun exit() {
        if (controller.isModelPresent() && askAboutSave())
            return
        this.currentStage?.close()
    }

    //Обработчик нажатия на кнопку "Экспорт"
    fun export() {
        val selectedDirectory = directoryChooser.showDialog(this.currentWindow)
        if (selectedDirectory != null)
            controller.export(selectedDirectory)
    }

    //Метод для заполнения полотна представлением выбранного графа редактируемой модели
    private fun fillCanvas() {
        val currentGraph:MLGraph = controller.currentGraph!!
        currentGraph.entities.forEach { addEntity(it) }
        currentGraph.ports.forEach { addPort(it) }
        currentGraph.relations.forEach { addRelation(it) }
    }

    //Обработчик нажатия на клавишу "Импорт"
    fun import() {
        val selectedDirectory = directoryChooser.showDialog(this.currentWindow) ?: return
        controller.import(selectedDirectory)
        updateAccessibleConstructs()
        fillCanvas()
        changeUiState(false)
        title = "${messages["title"]}: ${controller.currentModel!!.name}"
    }

    //Метод для заполнения панели с информацией о конструкции
    private fun initializeConstructInfo() {
        if (paneConstruct == SelectedConstruct.NOTHING) {
            constructAccordion.isDisable = true
            constructAccordion.expandedPane?.isExpanded = false
            constructIdLabel.text = ""
            constructNameField.text = ""
            constructBackColorPicker.value = null
            constructStrokeColorPicker.value = null
            return
        }
        val construct = controller.currentModel!!.constructs[paneConstructId]!!
        constructIdLabel.text = construct.id.toString()
        constructNameField.text = construct.name
        constructAccordion.isDisable = false
        val view = controller.getConstructView(paneConstructId!!)
        constructBackColorPicker.value = view!!.backColor.transform()
        constructStrokeColorPicker.value = view.strokeColor.transform()
    }

    //Обработчик нажатия на кнопку "Открыть"
    fun openModel() {
        val openDialog = OpenDialog()
        openDialog.openModal(modality = Modality.APPLICATION_MODAL, owner = this.currentWindow,
                block = true, resizable = false)?.showAndWait()
        if (openDialog.canceled)
            return
        controller.openModel(openDialog.selectedPrototype!!)
        title = "${messages["title"]}:${openDialog.selectedPrototype!!.name}"
        updateAccessibleConstructs()
        fillCanvas()
        changeUiState(false)
    }

    //Обработчик нажатия на кнопку "Сохранить"
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

    //Метод для обновления информации о выбранной на полотне конструкции
    private fun selectConstruct(construct: SelectedConstruct, id:UUID? = null, shape:Shape? = null) {
        paneConstruct = construct
        paneConstructView = if (id == null) null else controller.getConstructView(id)
        paneConstructId = id
    }

    //Обработчик нажатия на кнопку "О программе"
    fun showAbout() = showInfoDialog(Alert.AlertType.INFORMATION, messages["about.title"], messages["about.text"])

    //TODO: обработчик нажатия на кнопку "Справка"
    fun showHelp() {

    }

    //Вспомогательный метод для отображения информационного окна
    private fun showInfoDialog(type:Alert.AlertType, title:String, text:String) {
        val alert = Alert(type); alert.headerText = null; alert.graphic = null
        alert.title = title; alert.contentText = text
        alert.dialogPane.minHeight = Region.USE_PREF_SIZE
        alert.showAndWait()
    }

    //Метод для обновления палитры конструкций
    //В соответствии с выбранным графом
    private fun updateAccessibleConstructs() {
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