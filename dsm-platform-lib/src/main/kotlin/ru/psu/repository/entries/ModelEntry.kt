package ru.psu.repository.entries

import java.util.*

data class ModelEntry(
        val prototypeId: UUID?, //Id прототипа
        val id: UUID, //Id модели
        val name:String, //Название модели
        val description:String //Описание модели
)