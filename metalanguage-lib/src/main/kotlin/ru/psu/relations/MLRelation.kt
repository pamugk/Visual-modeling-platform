package ru.psu.relations

import ru.psu.attributes.MLAttribute
import ru.psu.constraints.MLConstraint
import ru.psu.constructs.MLConstruct
import java.util.*
import kotlin.collections.ArrayList

class MLRelation(
        id: UUID, name: String, prototypeId: UUID?, innerStructure: UUID?,
        var type:MLRelTypes, var unique:Boolean, var multiplicity:MLMultiplicity
): MLConstruct(id, name, prototypeId, innerStructure) {
    val attributes:MutableList<MLAttribute> = ArrayList()
    val constraints:MutableList<MLConstraint> = ArrayList()
}