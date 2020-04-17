package ru.psu.transformer

import ru.psu.graphs.MLGraph
import ru.psu.model.Model
import java.util.*

class ModelTransformer {
    fun transformModel(model: Model?, dslFrom: Model?, dslTo: Model?): Model? {
        return Model(null, UUID.randomUUID(), "", "", MLGraph(null, UUID.randomUUID(),  null))
    }
}