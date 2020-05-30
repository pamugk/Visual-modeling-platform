package ru.psu.constraints

//Перечисление всех видов сравнений для ограничений в MetaLanguage 1.1
enum class MLComparisons {
    EQUALS, //Равно
    GREATER, //Больше
    LESS, //Меньше
    LEQUALS, //Меньше или равно
    GREQUALS //Больше или равно
}