package ru.psu.view.auxiliaries

import java.io.Serializable

//Класс для хранения информации о шрифте
data class FontDto(var name:String, var size:Double,
              var weight: Weight = Weight.NORMAL, var posture:Posture = Posture.NORMAL): Serializable {
    enum class Weight{ BLACK, BOLD, EXTRA_BOLD, EXTRA_LIGHT, LIGHT, MEDIUM, NORMAL, SEMI_BOLD, THIN }
    enum class Posture { ITALIC, NORMAL }
}