package ru.psu.entities

import ru.psu.attributes.MLAttribute
import ru.psu.constraints.MLConstraint
import ru.psu.constructs.MLConstruct
import java.util.*
import kotlin.collections.ArrayList

//Класс сущности в MetaLanguage 1.1
class MLEntity(
        parentId:UUID?, //Идентификатор конструкции-родителя
        id: UUID, //Id сущности
        name: String, //Название
        prototypeId: UUID?, //Id прототипа (нет - сущность в метамодели, есть - экз. сущности)
        innerStructure: UUID?, //Id графа, описывающего внутреннюю структуру
        maxCount:Int //Максимальное число экземпляров
): MLConstruct(parentId, id, name, prototypeId, innerStructure, maxCount) {
    val ports:MutableSet<UUID> = HashSet() //Порты сущности
    val attributes:MutableList<MLAttribute> = ArrayList() //Атрибуты сущности
    val constraints:MutableList<MLConstraint> = ArrayList() //Ограничения на сущность
}