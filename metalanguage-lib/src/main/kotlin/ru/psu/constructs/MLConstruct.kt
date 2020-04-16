package ru.psu.constructs

import java.util.*

//Класс конструкции MetaLanguage 1.1
abstract class MLConstruct(
        var parentId:UUID?, //Идентификатор сущности-родителя
        val id: UUID, //Идентификатор
        var name:String, //Название
        val prototypeId:UUID?, //Прототип (если null, то это конструкция в метамодели)
        val innerStructure:UUID? //Id графа, описывающего внутреннюю структуру (если есть)
)