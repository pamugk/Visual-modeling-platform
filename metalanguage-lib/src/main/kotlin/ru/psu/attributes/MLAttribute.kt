package ru.psu.attributes

import ru.psu.constraints.MLConstraint
import java.io.Serializable

//Класс атрибута в MetaLanguage 1.1
class MLAttribute(
        var type:MLTypes,  //Тип атрибута
        var name:String, //Имя атрибута
        var value:String, //Значение
        var defaultValue:String, //Значение по умолчанию
        var description:String, //Описание
        val constraints:MutableList<MLConstraint> = ArrayList() //Ограничения на атрибут
): Serializable