package ru.psu.relations

import ru.psu.attributes.MLAttribute
import ru.psu.constructs.MLConstruct
import java.util.*
import kotlin.collections.ArrayList

//Класс отношения в MetaLanguage 1.1
class MLRelation(
        parentId:UUID?, //Идентификатор конструкции-родителя
        id: UUID, //Id отношения
        name: String, //Название отношения
        prototypeId: UUID?, //Id прототипа
        innerStructure: UUID?, //Id графа, описывающего внутреннюю структуру
        var type:MLRelTypes, //Тип отношения
        maxCount:Int, //Максимальное число экземпляров
        var multiplicity:MLMultiplicity, //Множественность отношения
        val ports:MutableSet<UUID> //Id связываемых портов
): MLConstruct(parentId, id, name, prototypeId, innerStructure, maxCount) {
    val attributes:MutableList<MLAttribute> = ArrayList() //Атрибуты отношения
}