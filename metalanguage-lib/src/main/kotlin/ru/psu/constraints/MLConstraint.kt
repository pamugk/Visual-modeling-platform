package ru.psu.constraints

import java.io.Serializable
import java.util.*

//Класс ограничения в MetaLanguage 1.1
class MLConstraint(
        var attributeId: UUID, //Идентификатор ограничения
        var comparedAttr: String, //Сравниваемый атрибут
        var comparsion: MLComparsions, //Тип сравнения со значением
        var value: String, //Значение, с которым сравнивается
        var name:String, //Название ограничения
        var description:String //Описание ограничения
): Serializable