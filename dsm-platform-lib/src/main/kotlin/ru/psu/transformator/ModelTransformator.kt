package ru.psu.transformator

import ru.psu.model.Model
import java.util.*

class ModelTransformator {
    fun createModelByDsl(dsl: Model?): Model? {
        return Model(UUID.randomUUID(), "")
    }

    fun transformModel(model: Model?, dslFrom: Model?, dslTo: Model?): Model? {
        return Model(UUID.randomUUID(), "")
    }
}