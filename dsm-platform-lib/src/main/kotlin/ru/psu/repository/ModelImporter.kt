package ru.psu.repository

import ru.psu.model.Model

interface ModelImporter {
    fun importModel(fileName:String): Model
}