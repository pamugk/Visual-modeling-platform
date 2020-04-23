package ru.psu.view.auxiliaries

class FontDto(var name:String, var size:Double,
              var weight: Weight = Weight.NORMAL, var posture:Posture = Posture.NORMAL) {
    enum class Weight{ BLACK, BOLD, EXTRA_BOLD, EXTRA_LIGHT, LIGHT, MEDIUM, NORMAL, SEMI_BOLD, THIN }
    enum class Posture { ITALIC, NORMAL }
}