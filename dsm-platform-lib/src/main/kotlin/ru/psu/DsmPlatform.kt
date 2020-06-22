package ru.psu

import ru.psu.attributes.MLAttribute
import ru.psu.entities.MLEntity
import ru.psu.generator.DslDefGenerator
import ru.psu.graphs.MLGraph
import ru.psu.model.Model
import ru.psu.ports.MLPort
import ru.psu.ports.MLPortKinds
import ru.psu.relations.MLMultiplicity
import ru.psu.relations.MLRelTypes
import ru.psu.relations.MLRelation
import ru.psu.repository.ModelRepository
import ru.psu.transformer.ModelTransformer
import ru.psu.transformer.basic
import ru.psu.transformer.move
import ru.psu.validator.ModelValidator
import ru.psu.view.ConstructView
import ru.psu.view.View
import ru.psu.view.auxiliaries.ColorDto
import ru.psu.view.auxiliaries.FontDto
import ru.psu.view.auxiliaries.StrokeDto
import ru.psu.view.auxiliaries.shapes.*
import java.util.*
import kotlin.collections.HashSet

private val defaultUUID:UUID = UUID.nameUUIDFromBytes("uuid://defaultUUID".toByteArray(Charsets.UTF_8))
private val defaultEntityUUID:UUID = UUID.nameUUIDFromBytes("uuid://Model".toByteArray(Charsets.UTF_8))
private val defaultAssociationUUID:UUID = UUID.nameUUIDFromBytes("uuid://Association".toByteArray(Charsets.UTF_8))
private val defaultInheritanceUUID:UUID = UUID.nameUUIDFromBytes("uuid://Inheritance".toByteArray(Charsets.UTF_8))
private val defaultPortUUID:UUID = UUID.nameUUIDFromBytes("uuid://Port".toByteArray(Charsets.UTF_8))

//Метод для создания сущности
fun Model.createEntity(graphId:UUID, name: String, id:UUID = UUID.randomUUID(), prototype: MLEntity? = null,
                       maxCount:Int = -1):UUID {
    val entity = MLEntity(graphId, id, name, prototype?.id, null, maxCount)
    prototype?.attributes?.forEach {
        entity.attributes.add(MLAttribute(it.type, it.name, it.defaultValue, it.defaultValue, it.description))
    }
    this.constructs[entity.id] = entity
    this.graphs[graphId]!!.entities.add(entity.id)
    return entity.id
}

//Метод для создания порта
fun Model.createPort(entityId:UUID, name:String, kind:MLPortKinds, id:UUID = UUID.randomUUID(),
                     prototypeId: UUID? = null, maxCount:Int = 1):UUID {
    val port = MLPort(entityId, id,  name, prototypeId, kind, maxCount)
    val entity:MLEntity = this.constructs[entityId] as MLEntity
    entity.ports.add(port.id)
    this.graphs[entity.parentId]!!.ports.add(port.id)
    this.constructs[port.id] = port
    return port.id
}

//Метод для создания связи
fun Model.createRelation(graphId:UUID, name: String, type:MLRelTypes, ports:List<UUID>, id:UUID = UUID.randomUUID(),
                         prototypeId: UUID? = null, maxCount:Int = -1,
                         multiplicity:MLMultiplicity = MLMultiplicity(1, 1)):UUID {
    val relation = MLRelation(graphId, id, name, prototypeId, null, type,
            if (type == MLRelTypes.INHERITANCE && id != defaultInheritanceUUID) 0 else maxCount, multiplicity, HashSet(ports))
    this.graphs[graphId]!!.relations.add(id)
    this.constructs[id] = relation
    return id
}

//Метод для создания граф.представления конструкции
fun View.addConstructView(id:UUID = UUID.randomUUID(), constructId:UUID, shape:ShapeDto,
                          backColor: ColorDto = ColorDto(1.0, 1.0, 1.0), content:String = "",
                          font: FontDto = FontDto("Arial", 12.0),
                          stroke: StrokeDto = StrokeDto(1.0),
                          strokeColor: ColorDto = ColorDto(0.0, 0.0, 0.0),
                          text:String = ""):UUID {
    val constructView = ConstructView(id, constructId, PointDto())
    constructView.backColor = backColor
    constructView.content = content
    constructView.font = font
    constructView.shape = shape
    constructView.stroke = stroke
    constructView.strokeColor = strokeColor
    constructView.content = text
    this.constructViews[id] = constructView
    return id
}

//Метод для создания граф.представления конструкции по прототипу
fun View.addConstructView(constructId:UUID, prototypeView:ConstructView, id:UUID = UUID.randomUUID(),
                          shift:PointDto, text:String = ""):UUID {
    val constructView = ConstructView(id, constructId, shift)
    constructView.backColor = prototypeView.backColor.copy()
    constructView.content = prototypeView.content
    constructView.font = prototypeView.font?.copy()
    constructView.shape = prototypeView.shape!!.basic().move(shift)
    constructView.stroke = prototypeView.stroke?.copy()
    constructView.strokeColor = prototypeView.strokeColor.copy()
    constructView.content = text
    this.constructViews[constructId] = constructView
    return id
}

fun View.addRelationView(constructId:UUID, prototypeView:ConstructView, ports: List<UUID>, id:UUID = UUID.randomUUID()):UUID {
    val view = this
    val positions = ports.map { view.constructViews[it]!!.position }
    val constructView = ConstructView(id, constructId, positions[0])
    constructView.backColor = prototypeView.backColor.copy()
    constructView.content = prototypeView.content
    constructView.font = prototypeView.font?.copy()
    constructView.shape = LineDto(positions[0].copy(), positions[1].copy())
    constructView.stroke = prototypeView.stroke?.copy()
    constructView.strokeColor = prototypeView.strokeColor.copy()
    this.constructViews[constructId] = constructView
    return id
}

//Класс DSM-платформы
class DsmPlatform(
        var defGenerator:DslDefGenerator,
        var validator:ModelValidator,
        var transformer:ModelTransformer,
        var repository:ModelRepository
) {
    //Метод создания модели
    fun createModel(metamodel:Model?, id: UUID = UUID.randomUUID(), graphId:UUID=UUID.randomUUID(),
                    name:String = "New model", description:String = "Recently created model"):Model {
        val graph = MLGraph(null, graphId, metamodel?.root)
        val model = Model(metamodel?.id, id, name, description, graphId)
        model.graphs[graphId] = graph
        return model
    }

    //Метод создания метамодели
    fun createView(model: Model, prototypeId:UUID?, name: String, description: String):View =
            View(UUID.randomUUID(), prototypeId, model.id, name, description)

    //Метод для получения модели, описывающей метаязык
    //Нужна сугубо для предоставления информации о том, что у сущности и порта один универсальный вид,
    //а у связей - два
    fun getMetalanguageModel(messages:ResourceBundle):Model {
        val metalanguage:Model = createModel(null, defaultUUID, defaultUUID,
                messages.getString("default.metalanguage.name"),
                messages.getString("default.metalanguage.description"))
        metalanguage.createEntity(defaultUUID, messages.getString("default.entity.name"), defaultEntityUUID)
        metalanguage.createPort(defaultEntityUUID, messages.getString("default.port.name"),
                MLPortKinds.BIDIRECTIONAL, id = defaultPortUUID, maxCount = -1)
        metalanguage.createRelation(defaultUUID, messages.getString("default.association.name"),
                MLRelTypes.ASSOCIATION, listOf(defaultPortUUID), defaultAssociationUUID)
        metalanguage.createRelation(defaultUUID, messages.getString("default.inheritance.name"),
                MLRelTypes.INHERITANCE, listOf(defaultPortUUID), defaultInheritanceUUID)
        return metalanguage
    }

    //Функция для предоставления стандартного граф. представления конструкций в метамодели
    //Используется как прототип при создании конструкции в метамодели
    fun getMetalanguageView(messages: ResourceBundle):View {
        val defaultMetamodelView = View(defaultUUID, null, defaultUUID,
                messages.getString("default.view.name"), messages.getString("default.view.description"))
        defaultMetamodelView.addConstructView(defaultEntityUUID, defaultEntityUUID,
                RectangleDto(PointDto(0.0, 0.0), 128.0, 128.0))
        defaultMetamodelView.addConstructView(defaultPortUUID, defaultPortUUID,
                EllipseDto(PointDto(0.0, 0.0), PointDto(16.0, 16.0)))
        defaultMetamodelView.addConstructView(defaultAssociationUUID, defaultAssociationUUID,
                LineDto(PointDto(0.0, 0.0), PointDto(32.0, 0.0)))
        defaultMetamodelView.addConstructView(defaultInheritanceUUID, defaultInheritanceUUID,
                LineDto(PointDto(0.0, 0.0), PointDto(32.0, 0.0)))
        return defaultMetamodelView
    }
}