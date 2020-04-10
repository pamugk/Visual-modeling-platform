package ru.psu.repository

import ru.psu.model.Model

interface ModelExporter {
    fun exportModel(fileName:String, model: Model):Boolean
}