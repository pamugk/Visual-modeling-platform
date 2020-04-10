package ru.psu.repository

import ru.psu.model.Model
import java.util.*
import kotlin.collections.ArrayList

class ModelRepository(var transferSystem: ModelTransferSystem) {
    val modelList:MutableList<Model> = ArrayList()

    fun saveModel(model:Model):Boolean {
        return false
    }

    fun loadModel(id: UUID):Model?{
        return null
    }
}