package ru.psu.graphs

import java.io.Serializable
import java.util.*
import kotlin.collections.HashSet

//Класс графа в MetaLanguage 1.1
class MLGraph(
        val parentId:UUID?, //Идентификатор конструкции-родителя
        val id: UUID, //Идентификатор
        val prototypeId:UUID? //Прототип (если null, то это конструкция в метамодели)
) : Serializable {
    val entities:MutableSet<UUID> = HashSet() //Id сущностей в графе
    val ports:MutableSet<UUID> = HashSet() //Id портов в графе
    val relations:MutableSet<UUID> = HashSet() //Id отношений в графе
}