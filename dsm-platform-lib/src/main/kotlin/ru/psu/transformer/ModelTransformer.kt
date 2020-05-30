package ru.psu.transformer

import ru.psu.graphs.MLGraph
import ru.psu.model.Model
import java.util.*

class ModelTransformer {
    fun transformModel(model: Model?, dslFrom: Model?, dslTo: Model?): Model? {
        val graph =  MLGraph(null, UUID.randomUUID(),  null)
        return Model(null, UUID.randomUUID(), "", "", graph.id)
    }
}