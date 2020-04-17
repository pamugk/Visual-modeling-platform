package ru.psu.model

import ru.psu.view.View
import ru.psu.constructs.MLConstruct
import ru.psu.graphs.MLGraph
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

//Класс для описания модели и метамодели
class Model(
        val prototypeId: UUID?, //Id прототипа (null в случае метамодели, UUID метамодели в случае модели)
        val id: UUID, //Id модели
        var name:String, //Название модели
        var description:String, //Описание модели
        val root:MLGraph //Корневой граф
) {
    val views: MutableList<View> = ArrayList() //Множество графических представлений модели/метамодели
    val graphs: MutableMap<UUID, MLGraph> = HashMap() //Множество графов
    val constructs: MutableMap<UUID, MLConstruct> = HashMap() //Множество конструкций
}