package ru.psu.view

import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

//Класс, соответствующий графическому представлению метамодели
class View(
        val id:UUID, //Id графического представления
        val prototypeId:UUID?, //Id графического представления
        val modelId:UUID, //Id связанной модели
        var name:String, //Название графического представления
        var description:String //Описание графического представления
): Serializable {
    //Ассоциативная коллекция, ставящая в соответствие UUID конструкции
    //её графическое представление
    val constructViews:MutableMap<UUID, ConstructView> = HashMap()
}