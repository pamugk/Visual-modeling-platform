package ru.psu.relations

import java.io.Serializable

//Класс, описывающий описание множественность отношения в MetaLanguage 1.1
class MLMultiplicity(
        var fromMult:Int, //От скольких
        var toMult:Int //К скольким
): Serializable 