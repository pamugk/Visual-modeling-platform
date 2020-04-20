package ru.psu.repository.entries

import java.util.*

//Класс, представляющий сводную информацию о граф. представлении, хранящемся в репозитории
data class ViewEntry(
        val id: UUID, //Id графического представления
        var name:String, //Название графического представления
        var description:String //Описание графического представления
)