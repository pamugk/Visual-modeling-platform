package ru.psu.view

import java.util.*
import kotlin.collections.HashMap

class View(val id:UUID, var name:String) {
    val constructViews:MutableMap<UUID, ConstructView> = HashMap()
}