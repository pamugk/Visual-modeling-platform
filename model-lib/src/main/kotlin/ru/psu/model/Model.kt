package ru.psu.model

import ru.psu.view.View
import ru.psu.constructs.MLConstruct
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Model(val id: UUID, var name:String) {
    val views: MutableList<View> = ArrayList()
    val constructs: MutableMap<UUID, MLConstruct> = HashMap()
}