package ru.psu.graphs

import ru.psu.constructs.MLConstruct
import java.util.*
import kotlin.collections.HashSet

//Класс графа в MetaLanguage 1.1
class MLGraph(
        parentId:UUID?, //Идентификатор конструкции-родителя
        id: UUID, //Идентификатор
        name:String, //Название
        prototypeId:UUID?, //Прототип (если null, то это конструкция в метамодели)
        innerStructure:UUID? //Id графа, описывающего внутреннюю структуру (если есть)
): MLConstruct(parentId, id, name, prototypeId, innerStructure) {
    val entities:MutableSet<UUID> = HashSet() //Id сущностей в графе
    val ports:MutableSet<UUID> = HashSet() //Id портов в графе
    val relations:MutableSet<UUID> = HashSet() //Id отношений в графе
}