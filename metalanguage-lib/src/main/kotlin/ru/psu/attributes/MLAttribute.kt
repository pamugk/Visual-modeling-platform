package ru.psu.attributes

//Класс атрибута в MetaLanguage 1.1
class MLAttribute(
        var type:MLTypes,  //Тип атрибута
        var name:String, //Имя атрибута
        var value:String, //Значение
        var defaultValue:String, //Значение по умолчанию
        var description:String //Описание
)