package ru.psu.view

import java.util.*
import kotlin.collections.HashMap

//Класс, соответствующий графическому представлению метамодели
class View(
        val id:UUID, //Id графического представления
        var name:String, //Название графического представления
        var description:String //
) {
    //Ассоциативная коллекция, ставящая в соответствие UUID конструкции
    //её графическое представление
    val constructViews:MutableMap<UUID, ConstructView> = HashMap()
}