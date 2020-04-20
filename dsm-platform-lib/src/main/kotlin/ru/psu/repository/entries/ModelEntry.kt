package ru.psu.repository.entries

import java.util.*

//Класс, представляющий сводную информацию о модели/метамодели, хранящейся в репозитории
data class ModelEntry(
        val prototypeId: UUID?, //Id прототипа
        val id: UUID, //Id модели
        val name:String, //Название модели
        val description:String //Описание модели
)