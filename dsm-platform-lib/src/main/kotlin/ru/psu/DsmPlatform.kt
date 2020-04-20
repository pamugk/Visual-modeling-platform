package ru.psu

import ru.psu.entities.MLEntity
import ru.psu.generator.DslDefGenerator
import ru.psu.graphs.MLGraph
import ru.psu.model.Model
import ru.psu.repository.ModelRepository
import ru.psu.transformer.ModelTransformer
import ru.psu.validator.ModelValidator
import ru.psu.view.View
import java.util.*

class DsmPlatform(
        var defGenerator:DslDefGenerator,
        var validator:ModelValidator,
        var transformer:ModelTransformer,
        var repository:ModelRepository
) {
    fun createMetamodel(name:String = "Metamodel", description:String = "") =
        Model(null, UUID.randomUUID(), name, description,
                MLGraph(null, UUID.randomUUID(), null))

    fun createModel(metamodel:Model, name:String = "Model", description:String = "") =
            Model(metamodel.id, UUID.randomUUID(), name, description,
                    MLGraph(null, UUID.randomUUID(), metamodel.root.id))

    fun createView(model: Model, prototypeId:UUID?, name: String, description: String):View =
            View(UUID.randomUUID(), prototypeId, model.id, name, description)
}