package ru.psu.constraints

import java.io.Serializable

//Класс ограничения в MetaLanguage 1.1
class MLConstraint(
        var comparison: MLComparisons, //Тип сравнения со значением
        var value: String, //Значение, с которым сравнивается
        var name:String, //Название ограничения
        var description:String //Описание ограничения
): Serializable