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
import ru.psu.validator.ModelValidator
import ru.psu.view.ConstructView
import ru.psu.view.View
import java.awt.*
import java.awt.BasicStroke.CAP_BUTT
import java.awt.BasicStroke.JOIN_ROUND
import java.awt.Font.PLAIN
import java.awt.geom.*
import java.util.*
import kotlin.collections.ArrayList

private val defaultUUID:UUID = UUID.nameUUIDFromBytes("uuid://defaultUUID".toByteArray(Charsets.UTF_8));
private val defaultEntityUUID:UUID = UUID.nameUUIDFromBytes("uuid://Model".toByteArray(Charsets.UTF_8))
private val defaultAssociationUUID:UUID = UUID.nameUUIDFromBytes("uuid://Association".toByteArray(Charsets.UTF_8))
private val defaultInheritanceUUID:UUID = UUID.nameUUIDFromBytes("uuid://Inheritance".toByteArray(Charsets.UTF_8))
private val defaultPortUUID:UUID = UUID.nameUUIDFromBytes("uuid://Port".toByteArray(Charsets.UTF_8))

fun Model.createAssociation(graphId:UUID, name: String, ports:List<UUID>, id:UUID = UUID.randomUUID(),
                            prototypeId: UUID? = null, maxCount:Int = -1,
                            multiplicity:MLMultiplicity = MLMultiplicity(-1, -1)):UUID {
    val associationRelation = MLRelation(graphId, id, name, prototypeId, null,
            MLRelTypes.ASSOCIATION, maxCount, multiplicity, ArrayList(ports))
    this.graphs[graphId]!!.relations.add(id)
    this.constructs[id] = associationRelation
    return id
}

fun Model.createEntity(graphId:UUID, name: String, id:UUID = UUID.randomUUID(), prototype: MLEntity? = null,
                       maxCount:Int = -1):UUID {
    val entity = MLEntity(graphId, id, name, prototype?.id, null, maxCount)
    prototype?.attributes?.forEach {
        entity.attributes.add(MLAttribute(it.type, it.name, it.defaultValue, it.defaultValue, it.description))
    }
    this.constructs[id] = entity
    this.graphs[graphId]?.entities?.add(id)
    return id
}

fun Model.createInheritance(graphId:UUID, name: String, ports:List<UUID>, id:UUID = UUID.randomUUID(),
                            prototypeId: UUID? = null, maxCount:Int = -1,
                            multiplicity:MLMultiplicity = MLMultiplicity(-1, -1)):UUID {
    val inheritanceRelation = MLRelation(defaultUUID, defaultInheritanceUUID, name, prototypeId,
            null, MLRelTypes.ASSOCIATION, -1, multiplicity, ArrayList(ports))
    this.graphs[graphId]!!.relations.add(id)
    this.constructs[id] = inheritanceRelation
    return id
}

fun Model.createPort(entityId:UUID, name:String, kind:MLPortKinds, id:UUID = UUID.randomUUID(),
                     prototypeId: UUID? = null, maxCount:Int = 1):UUID {
    val port = MLPort(defaultEntityUUID, defaultPortUUID,  name, prototypeId, kind, maxCount)
    val entity:MLEntity = this.constructs[entityId] as MLEntity
    this.graphs[entity.parentId]!!.ports.add(id)
    entity.ports.add(id)
    this.constructs[id] = port
    return id
}

fun View.addConstructView(id:UUID = UUID.randomUUID(), constructId:UUID, shape:Shape,
                          backColor:Color = Color.WHITE, content:String = "",
                          font: Font = Font("Arial", PLAIN, 12),
                          stroke: Stroke = BasicStroke(1.0F, CAP_BUTT, JOIN_ROUND),
                          strokeColor: Color = Color.BLACK):UUID {
    val constructView = ConstructView(id, constructId)
    constructView.backColor = backColor
    constructView.content = content
    constructView.font = font
    constructView.shape = shape
    constructView.stroke = stroke
    constructView.strokeColor = strokeColor
    this.constructViews[id] = constructView
    return id
}

fun View.addConstructView(constructId:UUID, prototypeView:ConstructView, id:UUID = UUID.randomUUID(),
                          transform:AffineTransform? = null):UUID {
    val constructView = ConstructView(id, constructId)
    constructView.backColor = prototypeView.backColor
    constructView.content = prototypeView.content
    constructView.font = prototypeView.font
    constructView.shape = GeneralPath(prototypeView.shape).createTransformedShape(transform)
    constructView.stroke = prototypeView.stroke
    constructView.strokeColor = prototypeView.strokeColor
    this.constructViews[constructId] = constructView
    return id
}

class DsmPlatform(
        var defGenerator:DslDefGenerator,
        var validator:ModelValidator,
        var transformer:ModelTransformer,
        var repository:ModelRepository
) {
    fun createModel(metamodel:Model?, id: UUID = UUID.randomUUID(), grapgId:UUID=UUID.randomUUID(),
                    name:String = "New model", description:String = "Recently created model") =
        Model(metamodel?.id, id, name, description,
                MLGraph(null, grapgId, metamodel?.root?.id))

    fun createView(model: Model, prototypeId:UUID?, name: String, description: String):View =
            View(UUID.randomUUID(), prototypeId, model.id, name, description)

    fun getMetalanguageModel(messages:ResourceBundle):Model {
        val metalanguage:Model = createModel(null, defaultUUID, defaultUUID,
                messages.getString("default.metalanguage.name"),
                messages.getString("default.metalanguage.description"))
        metalanguage.createEntity(defaultUUID, messages.getString("default.entity.name"), defaultEntityUUID)
        metalanguage.createPort(defaultEntityUUID, messages.getString("default.port.name"),
                MLPortKinds.BIDIRECTIONAL, maxCount = -1)
        metalanguage.createAssociation(defaultUUID, messages.getString("default.association.name"),
                listOf(defaultPortUUID), defaultAssociationUUID)
        metalanguage.createInheritance(defaultUUID, messages.getString("default.inheritance.name"),
                listOf(defaultPortUUID), defaultInheritanceUUID)
        return metalanguage
    }

    fun getMetalanguageView(messages: ResourceBundle):View {
        val defaultMetamodelView = View(defaultUUID, null, defaultUUID,
                messages.getString("default.view.name"), messages.getString("default.view.description"))
        defaultMetamodelView.addConstructView(defaultEntityUUID, defaultEntityUUID,
                Rectangle2D.Double(0.0, 0.0, 32.0, 32.0))
        defaultMetamodelView.addConstructView(defaultPortUUID, defaultPortUUID,
                Ellipse2D.Double(0.0, 0.0, 4.0, 4.0))
        defaultMetamodelView.addConstructView(defaultAssociationUUID, defaultAssociationUUID,
                Line2D.Double(0.0, 0.0, 32.0, 0.0))
        defaultMetamodelView.addConstructView(defaultInheritanceUUID, defaultInheritanceUUID,
                Line2D.Double(0.0, 0.0, 32.0, 0.0))
        return defaultMetamodelView
    }
}