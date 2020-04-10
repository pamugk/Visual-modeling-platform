package ru.psu.entities

import ru.psu.attributes.MLAttribute
import ru.psu.constructs.MLConstruct
import java.util.*
import kotlin.collections.ArrayList

class MLEntity(
        id: UUID, name: String, prototypeId: UUID?, innerStructure: UUID?,
        var maxCount:Long, var unique:Boolean
): MLConstruct(id, name, prototypeId, innerStructure) {
    val attributes:MutableList<MLAttribute> = ArrayList()
    val constraints:MutableList<MLAttribute> = ArrayList()
}