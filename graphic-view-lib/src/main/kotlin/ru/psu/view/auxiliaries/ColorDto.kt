package ru.psu.view.auxiliaries

import java.io.Serializable

//Класс для описания цвета
data class ColorDto(val r:Double, val g:Double, val b:Double, val a:Double = 1.0): Serializable