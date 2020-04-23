package ru.psu.constructs

import java.io.Serializable
import java.util.*

//Класс конструкции MetaLanguage 1.1
abstract class MLConstruct(
        var parentId:UUID?, //Идентификатор родителя
        val id: UUID, //Идентификатор
        var name:String, //Название
        val prototypeId:UUID?, //Прототип (если null, то это конструкция в метамодели)
        val innerStructure:UUID?, //Id графа, описывающего внутреннюю структуру (если есть)
        var maxCount:Int //Максимальное число экземпляров
): Serializable