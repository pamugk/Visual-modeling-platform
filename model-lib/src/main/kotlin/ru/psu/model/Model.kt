package ru.psu.model

import ru.psu.constructs.MLConstruct
import ru.psu.graphs.MLGraph
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

//Класс для описания модели и метамодели
class Model(
        var prototypeId: UUID?, //Id прототипа (null в случае метамодели, UUID метамодели в случае модели)
        var id: UUID, //Id модели
        var name:String, //Название модели
        var description:String, //Описание модели
        var root:UUID //Корневой граф
): Serializable {
    val graphs: MutableMap<UUID, MLGraph> = HashMap() //Множество графов
    val constructs: MutableMap<UUID, MLConstruct> = HashMap() //Множество конструкций
}